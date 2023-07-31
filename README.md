# ZocDoc 💊
ZocDoc is a medical android app which deals and solves issues of both patients and doctors. 

## Application Install

***You can Install and test latest ZocDoc app from below 👇***

[![ZocDoc](https://img.shields.io/badge/ZocDoc✅-APK-red.svg?style=for-the-badge&logo=android)](https://drive.google.com/file/d/13kE3sCELCoxvPaNxNXjsN7M7WQERLYnu/view?usp=share_link)

[Want to try the app](https://appetize.io/app/xtrewomqm4k4kugtyh7v7vpmeq?device=pixel4&osVersion=11.0&scale=75)

# Our Idea 💡
- ZocDoc is an Android application that not only solves the issue of the patients as users, but also solves the problems of the doctors as well.

- The app offers users to book an appointment with the doctor that are registered in the app already.

- The waiting queue is sorted according to the priority and not ‘First Come First Serve’ which allows all the patients to get equal priority.

- The users can upload their prescription which only keeps the latest upload which the doctor can check.

- The users can monitor their health data which is shown in a chart of last 5 data from their report.

# App Features 🎯

- <b>Appointment Booking:</b> The user can search doctors by their name, phone or email to book an appointment.
The users have to answer some questions regarding their issue, this way the appointment is booked.
- <b>Appointment Sorting:</b> We designed an alorithm that analyses the user data, makes some calculation and sorts the patient queue according to the priority. 
This way every patient gets priority equally.
- <b>Prescription:</b> The user uploads their latest prescrition in the app, so when booken an appointment, the doctor can check their recent 
prescription and hence carrying prescription is not required.
- <b>Statistics:</b> The user can add and monitor their past 5 data of health reports which is displayed using charts.
- <b>Instant UPI:</b> When asked about the fee amount, it sounds quite unprofessional for Doctors when they have to tell about it. 
But from now, the Doctor can add their fee details and the app generates a QR Code iteself.
- <b>Appointment / Patient Queue List:</b> The users can check their appointment list of all doctors to which they booked appointment. 
The patient queue is a list that is sorted using our alogrithm and displayed. 

## 📸 Screenshots 


<p align="center">
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219858356-b3119738-7267-40ed-a011-042fc85160f7.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219860016-56dd6cd6-cd9b-41a5-9b33-1d5bcfa2ec88.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219860970-c9d81b20-d6db-49fa-bbf0-3b03423cd03d.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219862031-9e821331-e2a9-46da-8c0d-9a8ca9d15994.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219863082-780d51c1-054b-4fa3-aa45-36d35bc1642d.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219863802-bdb0de70-5781-4823-a9bc-1aebdc738a71.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219863861-c3c3fbcb-6d81-4ca3-8e83-6ca883e35727.jpeg"/>
<img width="210" height="362" src="https://user-images.githubusercontent.com/74999138/219863899-d218b311-f61d-4cb7-b081-a0545e4bf49f.jpeg"/>
</p>


# Built With 🔩

- <b>[Kotlin](https://kotlinlang.org/docs/android-overview.html)</b> - We developed this project using kotlin in backend & XML to implement designs into code.
The app was developed in [Android Studio](https://developer.android.com/studio). We have also used some open source libraries like [ZXing for QR Code](https://github.com/zxing/zxing), [Chart](https://github.com/majorkik/SparkLineLayout) and some [UI libraries](https://material.io/).
- <b>[Java](https://developer.android.com/guide)</b> - As Java and Kotlin are interoperable, we used a Java class that encrypts the user data while saving in the app storage using <b>AES Encryption</b>. This way we prevent anydata leak.
- <b>[Firebase](https://firebase.google.com/docs/android/setup)</b> - Firebase is a free tool for developers so we used it to authenticate users using their email and perform similar operations. We also used firebase to store user data as well as their appointment details, and etc.
Not only that, we also used firebase to store docs that are uploaded by the  users (Prescriptions).
- <b>[Jetpack](https://developer.android.com/jetpack/?gclid=CjwKCAjwsfuYBhAZEiwA5a6CDNJYBqgSGZjiTgYNqfw0DhgCBrzwsWJh1Hvkr1tKuxDBKX_V8m7cahoCn_wQAvD_BwE&gclsrc=aw.ds)</b> - Jetpack framework is globally used in Android development nowadays.
  The libraries we used are:
  - Navigation Fragment
  - ViewBinding
- <b>[Figma](https://www.figma.com/)</b> - We used Figma to design the UI / UX during the designing & prototyping phase of our project.
The design we used is minimal as well as clean. People with colorbindness will have no issues using it.

# Project Setup 📝
- Clone the repository by the following link and open the project in Android Studio or Git Bash
- ```bash
  https://github.com/gaddarkumar7447/ZocDoc.git
- Connect it to your Firebase Account -
  - 1 [How to Disconnect Firebase Project?](https://stackoverflow.com/questions/38120862/remove-firebase-analytics-from-android-app-completely)
  - 2 [How to Disconnect Firebase Project?](https://stackoverflow.com/questions/51549554/how-to-completely-disconnect-an-android-app-from-firebase-in-android-studio)
- After removing the account, link to your own firebase project to test and use.
- Follow the above steps to create account and proceed
- **Important** (Please remove your googleservices.json before committing changes)

- Credit to [Aritra Das](https://github.com/aritra-tech) and [Binay Shaw](https://github.com/binayshaw7777/) for their exceptional work on [Medify](https://github.com/aritra-tech/Medify). This repository is a clone with additional changes.
