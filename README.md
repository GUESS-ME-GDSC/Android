# Guess me! (friend & family)

<p align="center">
 <br>
 <div width="400" style="background: none;" align="center">
  <img src='https://user-images.githubusercontent.com/86697585/243171405-2ac24af5-a4c3-422c-8712-b25e29eadd31.png' alt="Guess me Logo" width="700" />
 </div>
</p>
<p align="center"><a href="https://www.youtube.com/watch?v=FTT72MwJD6c">Demo Video</a>
</p>
<p align="center">
 <img alt="GitHub language count" src="https://img.shields.io/github/contributors/GUESS-ME-GDSC/Server?style=for-the-badge&logo">
 <img alt="GitHub language count" src="https://img.shields.io/github/issues-closed/GUESS-ME-GDSC/Server?style=for-the-badge&logo">
 <img alt="GitHub language count" src="https://img.shields.io/github/stars/GUESS-ME-GDSC/Server?style=for-the-badge&logo">
</p>

## 👋 Project Overview

**Guess me! (friend & family)** is a person information storage/writing quiz app for preventing memory loss and family memory of the elderly.

### **Goal 3. Good Health and Well-Being**

[THE 17 GOALS | Sustainable Development](https://sdgs.un.org/goals/goal3)

> Our solution focuses on the **United Nations' Sustainable Development goal of Good Health & Wellbeing**. Ensure healthy lives and promote well-being for all at all ages specifically targeting the elderly population.

<br>

> How can we prevent an increase in the incidence of dementia in the elderly population and help them maintain memories of their loved ones? Dementia is a progressive disease that affects millions of people worldwide and can be fatal not only to individuals but also to family and friends. As the world's population ages, the proportion of people with dementia is increasing, and this trend poses a significant challenge to individuals, families and communities around the world.

> Our project aims to develop ways to reduce the risk of dementia and promote healthy aging in the elderly population. The memory loss of a loved one due to dementia is a major problem that we are trying to solve. Handwriting and quiz-type repetitive learning have been shown to be effective in promoting brain activity, strengthening memory, and improving cognitive ability in the elderly. By encouraging these activities, we hope to reduce the risk of dementia and help individuals maintain memories of their loved ones.

> Ultimately, **Guess me! (friend & family)** aims to contribute to global efforts to improve the lives of individuals, families and communities around the world by preventing dementia and promoting healthy aging in the elderly. We hope to reduce the burden of dementia on individuals, families, and society as a whole by focusing on early prevention and intervention.<br>

> Furthermore, when entering information from close people while using the service, we want to create time to know what we like, what we like, and how we like clothes, and to make more valuable time to communicate through the process of taking pictures and recording voices.

## 📖 Table of Contents

<ol>
 <li><a href="#features">Features</a></li>
 <li><a href="#stacks">Stacks</a></li>
 <li><a href="#expectation">Expectation</a></li>
 <li><a href="#competitiveness">Competitiveness</a></li>
 <li><a href="#gettingstarted">Getting Started</a></li>
 <li><a href="#structure">Source Code Structure</a></li>
 <li><a href="#teaminfo">TEAM INFO</a></li>
</ol>

<h2 id="features"> ✨ Key Features </h2>

There is **Two key features** exist.

- **Register Person Information**
- **Quiz about registered people**

<h2 id="expectation"> ✨ Expectations </h2>
 
 ### 👍 Memory Enhancement
- You can promote brain activity by taking quizzes.
- It helps prevent dementia by submitting answers through writing.
 
 ### 🤝 Enabling Family Communication
- If you use the service, you will have time to communicate with your family.
- You can get to know each other more by entering your family's information.
 
<h2 id="stacks"> 🛠️  Tech Stacks </h2>

<img width="500" alt="Guessme_project_architecture" src="https://user-images.githubusercontent.com/86697585/243169504-cadfd2dc-7afd-4757-bace-34e4d96f650c.png">

### 🚉 Platform

- [Docker](https://www.docker.com/)
- [Google Cloud Platform](https://cloud.google.com/)
- [Google Cloud SQL](https://cloud.google.com/sql)
- [Google Cloud Storage](https://cloud.google.com/storage)

### 🦾 Server

- [JAVA 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Spring Boot v2.7.9](https://spring.io/projects/spring-boot)
- [MySQL](https://www.mysql.com/)
- [JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT](https://jwt.io/)

### 😄 ML(Image Compare Server)
- python 3.9.6
- tensorflow
- keras
- torch

### 😎 Android

- **Architecture**
  - MVVM
- **Android Jetpack**
  - ViewModel
  - LiveData
  - View Binding
  - Navigation
- **Network**
  - Retrofit2 / okHttp3
- **Asynchronous**
  - Kotlin Coroutine
- **Database**
  - DataStore
- **DI**
  - Hilt

<h2 id="gettingstarted"> 🏃 Getting Started </h2>



### 1. Repository Clone

```
git clone https://github.com/GUESS-ME-GDSC/Android.git
```

### 2. Run Emulator

You don't need to install anything to run Guessme.

👀 But there's one thing to watch out for!
> In the case of the ocr used for grading, when an image with no characters comes in, the error processing may not be perfect, resulting in an error! 

### → Please use an image containing characters!

<h2 id="teaminfo"> 👨‍👦‍👦 Team Info </h2>

<table width="500">
    <thead>
    </thead>
    <tbody>
    <tr>
        <th>Name</th>
        <td width="100" align="center">Kim HaeChan</td>
        <td width="100" align="center">Kim JeongHo</td>
        <td width="100" align="center">Mun yuri</td>
    </tr>
    <tr>
        <th>Role</th>
        <td width="150" align="center">
            Server
        </td>
        <td width="150" align="center">
            Server
        </td>
        <td width="150" align="center">
            Android
        </td>
    </tr>
    <tr>
        <th>GitHub</th>
        <td width="100" align="center">
            <a href="https://github.com/bluesun147">
                <img src="http://img.shields.io/badge/bluesun147-green?style=social&logo=github"/>
            </a>
        </td>
        <td width="100" align="center">
            <a href="https://github.com/hou27">
                <img src="http://img.shields.io/badge/hou27-green?style=social&logo=github"/>
            </a>
        </td>
        <td width="100" align="center">
            <a href="https://github.com/915dbfl">
                <img src="http://img.shields.io/badge/915dbfl-green?style=social&logo=github"/>
            </a>
        </td>
    </tr>
    <tr>
        <th>Email</th>
        <td width="175" align="center">
            <a href="mailto:er196725@googlemail.com">
                <img src="https://img.shields.io/badge/er196725@googlemail.com-green?logo=gmail&style=social">
            </a>
        </td>
        <td width="175" align="center">
            <a href="mailto:ataj125@gmail.com">
                <img src="https://img.shields.io/badge/ataj125@gmail.com-green?logo=gmail&style=social">
            </a>
        </td>
        <td width="175" align="center">
            <a href="mailto:myr1068@gmail.com">
                <img src="https://img.shields.io/badge/myr1068@gmail.com-green?logo=gmail&style=social">
            </a>
        </td>
    </tr>
    </tbody>
</table>
