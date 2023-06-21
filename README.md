# FreeTalk-ToyProject
### 커뮤니티의 RestAPI 서버 만들기

비슷한 관심사를 가진 사람들과 하나의 커뮤니티를 이룰수 있도록 해주는 서비스 입니다.

### 기능 명세
<details>
    <summary>회원 서비스</summary>
</br>
    
- 회원 가입/탈퇴, 이메일 인증, 로그인, 로그아웃, 정보 조회/수정
- 사용자 권한
    - 인증필요, 일반 사용자, 읽기 권한, 쓰기 권한, 모든 권한     
- (관리자)사용자 리스트 조회
    -  이름,닉네임,이메일 의 키워드로 검색 
    -  권한,상태,가입된 커뮤니티 등의 조건 검색 가능
</br>

- 예시 이미지- 사용자 리스트 조회</br>
  
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/4969e1d0-3339-4342-9a93-4ff22669ef60)
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/b295f0cc-fb9f-4ef0-a31f-ad4a5cf98269)

</details>
<details>
    <summary>카테고리 서비스</summary>
</br>
    
- Self join을 활용한 무한 depth의 카테고리 기능
- (관리자)카테고리 생성,수정,삭제
- 카테고리 조회 기능
</br>

- 예시 이미지- 카테고리 조회 일부분</br>
  
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/dcea0df5-b7bf-46d9-a677-1e34c6695ca3)


</br>

</details>
<details>
    <summary>커뮤니티 서비스</summary>
</br>
    
- 커뮤니티 CRUD,가입/탈퇴
- 사용자가 가입한 커뮤니티 조회
- 커뮤니티 검색
    - 최신순, 인원순, 게시글순 등의 동적 정렬 기능
    - 카테고리 조건 기능
    - 키워드 검색 기능 
</br>

-예시 이미지- 커뮤니티 단일 조회</br>

![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/95f83d0a-feee-4b64-b693-3d5b91ea0a27)



</br>

</details>

<details>
    <summary><span>★</span>게시글 서비스</summary>
</br>
    
- 게시글 CRUD( 커뮤니티 가입 후 원하는 커뮤니티에 게시)
- <span>★</span>게시글 조회
  - 다양한 검색 타입 지원
      - 제목, 내용, 닉네임, 제목&내용, 전부&HashTag 등의 동적인 타입 지원
  - 다양한 정렬 지원 
      - 최신순, 오래된 순, 조회수 순, 좋아요 순
  - 다양한 조건
      - 커뮤니티 조건, 작성자 조건, 최소 좋아요 조건, 최소 조회수 조건, 게시 날짜 조건, 게시글 종류 조건
  
</br>
  
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/e4ccb664-61e7-400a-ade9-dfd955723bb9)


  
- Self join을 활용한 무한 대댓글 기능과 CRUD</br>
</br>

- 예시 이미지- 게시글의 댓글 조회 일부분</br>
  
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/065539de-2b33-4159-8bf5-450d05997cec)

</br>

</details>

### 기술 명세

<details>
    <summary>회원 서비스</summary>
</br>
    
- Spring-Security와 JWT를 활용한 회원 인증 서비스 구현
- Redis를 활용한 토큰 인증 방식과, 이메일 인증 방식 구현
- Security의 Role을 Customzie하여 다양한 권한 구현( ex: 읽기 권한, 쓰기 권한)
- QueryDsl을 이용한 사용자 검색의 동적 쿼리 지원</br>
    
[회원 서비스 부분](https://github.com/cutegyuseok/FreeTalk-ToyProject/tree/master/src/main/java/com/cutegyuseok/freetalk/auth)</br>

</details>


## ERD</br>
![image](https://github.com/cutegyuseok/FreeTalk-ToyProject/assets/103543611/d4839a72-b232-4460-99c3-49f4084a2eaf)
