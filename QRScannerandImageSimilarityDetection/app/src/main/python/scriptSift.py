import cv2
import base64
from firebase import firebase
from PIL import Image
import base64
import io

def main (data,Category):

    fb = firebase.FirebaseApplication("https://graduation-project-b2644-default-rtdb.firebaseio.com/", None)

    decoded_data = base64.b64decode(data)
    np_data = np.fromstring(decoded_data, np.uint8)
    original = cv2.imdecode(np_data, cv2.IMREAD_UNCHANGED)

    # Sift and Flann
    sift = cv2.SIFT_create()
    kp_1, desc_1 = sift.detectAndCompute(original, None)

    index_params = dict(algorithm=0, trees=5)
    search_params = dict()
    flann = cv2.FlannBasedMatcher(index_params, search_params)
    all_images_to_compare = []
    titles=[]
    all_images=[]
    Ids=[]
    Getresult = fb.get('/LostItems/'+Category+'/',None)
    for keyId in Getresult:
        all_images.append(str(Getresult[keyId]['imageLosted']))
        Ids.append(str(Getresult[keyId]['user_ID']))
    for ImageId in all_images:
        b = bytes(ImageId, 'utf-8')
        filename = 'some_image.jpg'
        with open(filename, 'wb') as fn:
            fn.write(base64.decodebytes(b))
        img = cv2.imread("./some_image.jpg")
        titles.append(ImageId)
        all_images_to_compare.append(img)

    UserIds=[]
    id=0
    for image_to_compare, title in zip(all_images_to_compare, titles):####we also need to extract titles  ,,,, use zip to get more arrays with
        # 1) Check if 2 images are equals
        if original.shape == image_to_compare.shape:
            print("The images have same size and channels")
            difference = cv2.subtract(original, image_to_compare)
            b, g, r = cv2.split(difference)
            if cv2.countNonZero(b) == 0 and cv2.countNonZero(g) == 0 and cv2.countNonZero(r) == 0:
                UserIds.append(Ids[id] + "with" + "100")
                print("Similarity: 100% (equal size and channels)")
                break
        # 2) Check for similarities between the 2 images
        kp_2, desc_2 = sift.detectAndCompute(image_to_compare, None)
        matches = flann.knnMatch(desc_1, desc_2, k=2)
        good_points = []
        for m, n in matches:
            if m.distance > 0.89 * n.distance:
                good_points.append(m)
        number_keypoints = 0
        if len(kp_1) >= len(kp_2):
            number_keypoints = len(kp_1)
        else:
            number_keypoints = len(kp_2)
        percentage_similarity = len(good_points) / number_keypoints * 100
        if (int(percentage_similarity) > 80):
            buff = io.BytesIO()
            pil_im.save(image_to_compare, format="PNG")
            img_str = base64.b64encode(buff.getvalue())
            imgSrc = str(img_str, 'utf-8')

            UserIds.append(Ids[id] + "with" + str(int(percentage_similarity))+"with"+imgSrc)
        id = id + 1

    return userIds[0]