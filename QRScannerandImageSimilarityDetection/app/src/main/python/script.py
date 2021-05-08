import numpy as np
import cv2
from PIL import Image
import base64
import io
import time
def main(data):
      decoded_data=base64.b64decode(data)
      np_data=np.fromstring(decoded_data,np.uint8)
      img=cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)
      gray=cv2.cvtColor(img,cv2.COLOR_BGR2RGB)
      #gray=cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)
      edged = cv2.Canny(gray, 10, 250)
      kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (7, 7))
      closed = cv2.morphologyEx(edged, cv2.MORPH_CLOSE, kernel)
      #pil_im=Image.fromarray(edged)


      (cnts, hierarchy) = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
      idx = 0
      encoded_images = []
      imgee=""
      for c in cnts:
          x, y, w, h = cv2.boundingRect(c)
          if w > 50 and h > 50:
              idx += 1
              new_img = img[y:y + h, x:x + w]
              out=cv2.cvtColor(new_img,cv2.COLOR_BGR2RGB)
              pil_im = Image.fromarray(out)

              buff = io.BytesIO()
              pil_im.save(buff, format="PNG")
              img_str = base64.b64encode(buff.getvalue())
              imgSrc = str(img_str, 'utf-8')+" nazmy "
              imgee+=imgSrc
      #return imgSrc
      return imgee






      #buff=io.BytesIO()
      #pil_im.save(buff,format="PNG")
      #img_str=base64.b64encode(buff.getvalue())
      #return ""+str(img_str,'utf-8')