from PIL import Image
from config import parse_args
import codecs
import requests as req
from io import  BytesIO
import io


def imgToTxt(url):
    def writetxt_a(txt, path):
        with codecs.open(path, 'a', 'utf-8') as w:
            w.seek(0)
            w.truncate()
            w.write(txt)


    args = parse_args()

    def imgTotxt(url):

        im = Image.open(url)  # open pic file
        im = im.resize((700, 605))  # convert to mask size
        newname = url[:-4] + '.ppm'  # new name for png file
        im.save(newname)  # save picture to target path

        testtxt = newname + r' C:\Users\davy3\crm_vue1\MLapi\STARE\1st_labels_ah\666.ppm' \
                            r' C:\Users\davy3\crm_vue1\MLapi\STARE\mask\mask_0001.png '
        writetxt_a(testtxt,args.test_data_path_list)

    imgTotxt(url)
    print('processed is done, now segmentation is on ')





