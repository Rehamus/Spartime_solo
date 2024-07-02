# 👀 익명/기명 커뮤니티 웹 서버
<p align="center"><img src="https://github.com/Spartime/Spartime/assets/154594004/3b3cdb93-c3e3-4aae-bc70-36ef30465606" width=400></p>

이 웹 서버는 스파르타 코딩 클럽만을 위한 익명/기명 커뮤니티 서비스를 제공하기 위해 개발된 웹서버입니다.<br/>

이 서버는 누구나 게시물들을 조회할 수 있고, 회원가입을 완료한 사용자에 한하여 로그인을 통해 게시물과 댓글, 좋아요를 등록, 수정, 삭제할 수 있습니다.

이 서버는 IntelliJ, Java, SpringBoot를 사용하여 개발되었으며 백엔드 데이터베이스로는 Docker를 이용한 MySQL을 사용합니다.

각 기능은 RESTful API를 통해 구현되었으며, Client는 HTTP 요청을 통해 서버와 상호작용합니다.

데이터의 보안을 위해 인증된 사용자만 접근할 수 있도록 JWT를 사용하여 API 요청마다 사용자를 검증하고 본 서비스의 사용자일 경우에만 접근을 허용합니다.

<br/>

# 📜 Table
- [Team](#-Team)
- [Tech Stack](#-Tech-Stack)
- [Project Structure](#-Project-Structure)
- [Feature](#-Feature)
- [ERD](#-ERD)
- [API Document](#-API-Document)
- [Code Convention](#-Code-Convention)
- [Git Rules](#-Git-Rules)

<br/>

## 🤝 Team
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/Berithx"><img src="https://avatars.githubusercontent.com/u/154594004?v=4" width="100px;" alt=""/><br /><sub><b> 팀장 : 이유환 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/namssanghyeok"><img src="https://avatars.githubusercontent.com/u/155861999?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 남상혁 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/hyun1202"><img src="https://avatars.githubusercontent.com/u/60086998?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 정현경 </b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/Rehamus"><img src="https://avatars.githubusercontent.com/u/161007461?v=4" width="100px;" alt=""/><br /><sub><b> 팀원 : 한정운 </b></sub></a><br /></td>
    </tr>
  </tbody>
</table>
<br/>

<details>
<summary><big>이유환</big></summary>
<div markdown="1">

- User
    - 회원탈퇴 API
    - Profile 조회, 수정 API
    - Follow 기능 구현
- JwtService
    - Reissue API
</div>
</details>

<details>
<summary><big>남상혁</big></summary>
<div markdown="1">

- Global
    - ExceptionHandler
      - 공통 BusinessException
    - AOP & Filter를 활용한 logging 및 공통 Response
      - `@Envelop` Annotation
      - tracelog 
    - 개발 환경 개선
      - `@LoginUser` Annotation
- Back Office
    - 공지사항 API
    - User Status Update API
    - User Authority Update API
    - User 전체 조회
</div>
</details>

<details>
<summary><big>정현경</big></summary>
<div markdown="1">

- Comment
    - Comment CRUD API
    - Comment Like 기능
</div>
</details>

<details>
<summary><big>한정운</big></summary>
<div markdown="1">

- User
    - Social Login
- Post
    - Post CRUD API
    - Post Like 기능
</div>
</details>

<details>
<summary><big>공동 개발</big></summary>
<div markdown="1">

- 회원가입 API
- 로그인 API
- AuthenticationFilter
- UserPrincipal
- JwtService
</div>
</details>

[(Back to top)](#-table)

<br/>

## 🤖 Tech Stack
|     Type     |                                                                                                           Tech                                                                                                            |        Version        |                                    Comment                                    |
|:------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------:|:-----------------------------------------------------------------------------:|
| IDE / EDITOR |                                               ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)                                               |           -           |                                       -                                       |
|  Framework   |                                                     ![Spring](https://img.shields.io/badge/springBoot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)                                                      |         3.3.0         |                                       -                                       |
|   Language   |                                                         ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)                                                         |        JDK 17         |                                       -                                       |
|   Database   | ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) <br/> ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) |   MySQL ver.8.0.28    |                                       -                                       |
|    Record    |                                                       ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)                                                        |           -           | [Link](https://teamsparta.notion.site/13-Al-a5ef9a2f5c514f9aa607119d86621c8a) |

[(Back to top)](#-table)

<br/>

## 🧱 Project Structure
```
Spartime
src
├── aop
│   ├── envelop
│   └── tracelog
├── dto
│   ├── request
│   └── response
├── entity
│   └── common
├── exception
├── repository
├── security
│   ├── config
│   ├── exception
│   ├── filter
│   ├── principal
│   ├── service
│   └── response
├── service
│   └── admin    
└── web
    ├── argumentResolver
    ├── config
    ├── controller
    ├── exception
    └── filter
    
```
[(Back to top)](#-table)

<br/>

## 🚀 Feature

- 조회를 제외한 모든 기능은 회원가입, 로그인을 통해 인증이 완료되어야만 접근할 수 있습니다.
- 이를 위해 JWT를 사용하였고 정상 로그인 처리가 되면 서버는 AccessToken(이하 AT)과 RefreshToken(이하 RT)를 발행하여 Body를 통해 반환하고 Client는 AT를 사용하여 서버에 접근해야 정상 처리됩니다.
- 또한, 서버의 보안을 위하여 AT의 유효시간을 짧게 설정하고, Expire된 AT를 서버로 요청할 경우 특정 HTTP 상태 코드를 반환하고 Client는 이를 신호로 RT를 사용하여 Reissue API를 호출하고 RT의 유효성을 확인한 후 AT를 재발급하는 절차를 사용하여 AT가 공격자에 의해 탈취된 경우를 대비합니다.
- User, Post, Comment 등 모든 데이터는 MySQL DB에 저장되어 관리됩니다.

[(Back to top)](#-table)

<br/>

## 🔗 ERD
<img src="https://github.com/Spartime/Spartime/assets/154594004/12978a7a-246b-4f90-bad3-82421edf5258">

[(Back to top)](#-table)

<br/>

## 💥 API Document
<img src="https://github.com/Spartime/Spartime/assets/154594004/3417a32d-a674-422f-b90a-6f2e3f228610">

[(Back to top)](#-table)

<br/>

## ⚖️ Code Convention
<img src="https://github.com/Spartime/Spartime/assets/154594004/be52a68f-2e3b-4637-abce-a03c60818aec">

[(Back to top)](#-table)

<br/>

## 📏 Git Rules
<img src="https://github.com/Spartime/Spartime/assets/154594004/608313b5-44fc-4ed9-a16c-173662c334bc">

[(Back to top)](#-table)