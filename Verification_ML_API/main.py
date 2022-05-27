from flask import Flask, request, render_template, flash, redirect
from numpy import frombuffer,reshape, argmax, maximum,max, uint8, arange, zeros, sum
from base64 import b64decode, b64encode
import json
import os
import cv2
import dlib
import face_recognition
from datetime import datetime



UPLOAD_FOLDER = 'uploads/'

# app
app = Flask(__name__)
app.secret_key = "secret keyas"
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER


# routes
@app.route('/')
def predict():
    return render_template("index.html")

@app.route('/prediction',methods=["POST"])
def prediction():
    print(request)
    # data = request.json['input_image']
    # r = b64decode(data)
    # q = frombuffer(r, dtype=uint8)
    # print(q.shape)
    # q = reshape(q, (1,512, 512,1))
    # print(q.shape)
    # q = q.astype('float32')
    # bdata = b64encode(image).decode()
    # payload = {"predicted_class" : "{}".format(predicted_class),"saliency_map" : bdata}
    # return json.dumps(payload)

@app.route('/upload_image',methods=["POST"])
def upload_image():
    print(datetime.now())
    if 'images[]' not in request.files:
        flash('No file part')
        return redirect(request.url)
    files = request.files.getlist('images[]')
    file_names = []
    for file in files:
        filename = file.filename
        file_names.append(filename)
        file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    #return json.dumps({file_names})
    
    results = zeros((len(file_names), len(file_names)), uint8)

    i=0
    for image in file_names:
        path = "uploads/{}".format(image)
        image_to_be_matched = face_recognition.load_image_file(path)
        image_to_be_matched_encoded = face_recognition.face_encodings(image_to_be_matched)[0]
        j=0
        redundant_check=0
        first_time=1
        for image_to_check in file_names:
            if(image_to_check==image):
                continue
            current_image = face_recognition.load_image_file("uploads/" + image_to_check)
            current_image_encoded = face_recognition.face_encodings(current_image)[0]
            result = face_recognition.compare_faces([image_to_be_matched_encoded], current_image_encoded)

            if result[0] == True:
                # print("Matched image {} with {} ".format(image,image_to_check))
                results[i][j]=1
            j=j+1
        i=i+1
    total_sum = 0
    for i in range(len(file_names)):
        for j in range(len(file_names)):
            if(results[i][j]==results[j][i]):
                results[j][i]=0

    no_of_problems = sum(results)
    print(datetime.now())

    for image in file_names:
        path = "uploads/{}".format(image)
        os.remove(path)

    return json.dumps({"Problems":"{}".format(int(no_of_problems))})


if __name__ == '__main__':
    app.run(threaded = 'True', debug=True)
