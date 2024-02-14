from langchain.prompts import ChatPromptTemplate, SystemMessagePromptTemplate, HumanMessagePromptTemplate
from langchain.schema import SystemMessage, HumanMessage
from langchain_openai import ChatOpenAI
import openai
import os
from prisma.models import User

# OpenAI API(유료, API 키 값은 보안을 위해 .env에 입력)
openai.api_key = os.getenv("OPENAI_API_KEY")
# DeepL API key(무료 버전, 월 50만 토큰까지 무료 번역) 
import deepl
deepl_key = "3d58744a-d5cd-fd8c-e078-015c393b54f4:fx"
translator = deepl.Translator(deepl_key)

# 초기값
elapsedTime = 0   # 총 답변 시간
R_list = ["전혀 그렇지 않다.", "그렇지 않다.", "보통이다.", "그렇다.", "매우 그렇다."] # 답변지
# 비판적사고 능력 질문지
Q_list1 = ["내가 한 행동이 나중에 어떤 결과를 가져올지 예상할 수 있다.",
        "나는 어떤 일을 하고 있을 때 그 일의 부분들이 마지막 목표에 어떤 영향을 주는지 알 수 있다.",
        "나는 다른 사람들이 말을 할 때 앞뒤가 맞는지를 따져본다.",
        "나는 복잡하고 어려운 글을 읽고 이해하려고 애쓰는 것이 중요하다고 생각한다.",
        "나는 어떤 사건에 대해 알맞은 근거를 찾아 객관적으로 판단하고 평가한다.",
        "나는 어떤 의견에 대해서 결정할 때 다양한 방법을 생각하고 판단한다."
        ]
# 의사소통 능력 질문지  
Q_list2 = [
    "나는 친구의 기분을 이해하려고 노력한다 나는 대화할 때 이야기를 잘 듣고 있다는 것을 말이나몸짓으로 보여준다.",
    "나는 친구의 마음(생각과 감정)을 잘 알 수 있다.",
    "나는 친구들의 고민을 잘 들어 준다.",
    "나는 친구가 선생님께 칭찬을 받으면 나도 기분이 좋아진다 .",
    "나는 상대방의 표정과 몸짓을 살피면서 속마음을 이해한다 .",
    "나는 대화할 때 이야기를 잘 듣고 있다는 것을 말이나 몸짓으로 보여준다 .",
    "나는 대화를 할 때 어떻게 말할지 미리 생각하고 말한다 .",
    "나는 듣는 사람이 이해할 수 있도록 쉽고 정확한 말을 골라 이야기 한다 .",
    "나는 듣는 사람이 잘 이해할 수 있도록 예를 들어 설명한다."
]
# 창의적사고 능력 질문지
Q_list3 = [
    "나는 현재에 만족하지 않고 새로운 일에 도전하는 것을 좋아한다 .",
    "나는 현실에서 일어날 수 없는 일을 자주 상상한다 .",
    "나는 아무생각 없이 같은 일을 계속 반복하는 것을 싫어한다 .",
    "나는 친구들에 비해 질문이 많은 편이다.",
    "나는 그동안 경험하지 못한 일을 할 기회가 생기면 해보고 싶다.",
    "나는 궁금한 것을 못 참는다."
]
QR_list = [] # 질문 + 답변 값을 담을 리스트
subject = ["다음 질문들은 설문자의 '비판적사고 능력'을 파악하는 질문들입니다.",
           "다음 질문들은 설문자의 '의사소통 능력'을 파악하는 질문들입니다.", 
           "다음 질문들은 설문자의 '창의적사고 능력'을 파악하는 질문들입니다." ]  # 큰 주제

# 설문에 따른 넛지 생성 함수
def send_survey_results(all_sentence):
    # OpenAI GPT-3.5 Turbo을 사용하여 챗봇 응답 생성
    # 챗봇 역할 지정
    system_message_template = SystemMessagePromptTemplate.from_template(
        template=f"""You are responding to a survey.
        Briefly analyze and summarize how reliable the respondent's answer is based on the conditions below.
        1. It takes approximately 30 seconds to complete the survey. If it takes less than this, it is highly likely
        that the person skimmed through the questions and skipped over them.
        2. If the same answer was given to all questions, there is a high possibility that the person skipped this survey.
        3. This survey is about critical thinking skills. If this person answers that their critical thinking is very high in one question, 
        but answers that their critical thinking is very low in another question, this person may have skimmed the survey.
        *The nudge form is to speak like a woman, like a woman, and if you are a man, like a man, speak briefly and briefly summarize the person's answer.
        """ )

    # 사람 입력 데이터 양식
    human_message_template = HumanMessagePromptTemplate.from_template(template="{text}")

    # 챗봇 프롬프팅
    chat_prompt = ChatPromptTemplate.from_messages([system_message_template, human_message_template])

    # 모든 질문 + 답변 영어로 번역 
    all_sentence_t = translator.translate_text(all_sentence, target_lang='EN-US').text

    messages = chat_prompt.format_prompt(text=all_sentence_t).to_messages()
    messages_dict = []
    for message in messages:
        if isinstance(message, SystemMessage):
            messages_dict.append({"role": "system", "content": message.content})
        elif isinstance(message, HumanMessage):
            messages_dict.append({"role": "user", "content": message.content})
   
    # 챗봇 응답 생성 및 번역
    response = openai.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=messages_dict,
        max_tokens=1000,
        n=1,
        stop=None,
        temperature=0.7,
        top_p=1.0,
        frequency_penalty=0.0,
        presence_penalty=0.0
    )
    chatbot_response = response.choices[0].message.content.strip()
    chatbot_responset = translator.translate_text(chatbot_response, target_lang='ko').text 
    print(chatbot_responset)
    return chatbot_responset

# fast api 사용 모듈
from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware

# prisma 사용 모듈
import asyncio
from prisma import Prisma
from typing import Optional
from pydantic import BaseModel

# 백엔드로부터 받을 데이터(예상)
class Item(BaseModel):
    id : int | None = None          # auto increment
    userId : int        # Spring에서 받아오는 id
    Q1 : int            # 번호
    Q2 : int
    Q3 : int
    Q4 : int
    Q5 : int
    Q6 : int
    elapsedTime : int      # 총 답변 시간

# FastAPI 클래스 객체 생성
app = FastAPI()

# 백엔드 서버 주소
# origins = [
#     "http://localhost:3000",
#     # "http://127.0.0.1:3000",
# ]
# app.add_middleware(
#     CORSMiddleware,
#     allow_origins=origins,
#     allow_credentials=True,
#     allow_methods=["*"],
#     allow_headers=["*"],
# )

# REST API
# 시작 화면
@app.get("/")
def hello():
    return {"message": "Hello AI Server"}

# user 테이블 읽기
@app.get("/read")
def read_user():
    user = asyncio.run(read_user_db())
    return user
async def read_user_db() -> None:
    db = Prisma()
    await db.connect()

    user = await db.user.find_many(
        where={
            'id': 1
        }
    )
    assert user is not None    
    await db.disconnect()
    return user

# 유저 테이블 값 입력
@app.post("/create")
def create_user(item: Item):
    asyncio.run(create_user_db(item))
    return {"message": "쓰기 성공"}

async def create_user_db(item: Item) -> None:
    db = Prisma()
    await db.connect()
    item_dict = item.model_dump()
    user = await db.user.create(
        {
        'userId': item_dict['userId'],
        'Q1': item_dict['Q1'],
        'Q2': item_dict['Q2'],
        'Q3': item_dict['Q3'],
        'Q4': item_dict['Q4'],
        'Q5': item_dict['Q5'],
        'Q6': item_dict['Q6'],
        'elapsedTime': item_dict['elapsedTime']
        }        
    )
    print(f'created user: {user.model_dump_json(indent=2)}')

    found = await db.user.find_unique(where={'id': user.id})
    assert found is not None  
    print(f'found user: {found.model_dump_json(indent=2)}')

    await db.disconnect()
    
# 유저 테이블 수정
@app.put("/create/{path}")
def put_user(path: int, item: Item):
    print(f'path string: {path}')
    asyncio.run(put_user_db(path, item))
    return {"message": "갱신 성공"}
async def put_user_db(path: int, item: Item) -> None:
    db = Prisma()
    await db.connect()
    item_dict = item.model_dump()
    user = await db.user.update(
        where={
                'id': path,
            },
        data={
        'userId': item_dict['userId'],
        'Q1': item_dict['Q1'],
        'Q2': item_dict['Q2'],
        'Q3': item_dict['Q3'],
        'Q4': item_dict['Q4'],
        'Q5': item_dict['Q5'],
        'Q6': item_dict['Q6'],
        'elapsedTime': item_dict['elapsedTime']
        }, 

    )
    print(f'updated user: {user.model_dump_json(indent=2)}')

    found = await db.user.find_unique(where={'id': user.id})
    assert found is not None  
    print(f'found user: {found.model_dump_json(indent=2)}')

    await db.disconnect()  

# 유저 테이블 삭제
@app.delete("/create/{path}")
def delete_user(path: int):
    print(f'delete string: {path}')
    asyncio.run(delete_user_db(path))
    return {"message": "삭제 성공"}

async def delete_user_db(path: int, item: Item) -> None:
    db = Prisma()
    await db.connect()
    user = await db.user.delete(
        where={
                'id': path,
            },
    )
    print(f'deleted user: {user.model_dump_json(indent=2)}')

    await db.disconnect()

# 넛지 생성    
@app.post("/create/answer")
def create_user(item: Item):
    user = asyncio.run(create_user_answer(item))
    return user

async def create_user_answer(item: Item) -> None:
    item_dict = item.model_dump()
    db = Prisma()
    await db.connect()
    found = await db.user.find_first(where={'userId': item_dict['userId']})
    print(found)
    # 기존에 이미 답변이 존재한다면 수정
    if found:
        user = await db.user.update_many(
            where={
                    'userId': item_dict['userId']
                },
            data={
            'Q1': item_dict['Q1'],
            'Q2': item_dict['Q2'],
            'Q3': item_dict['Q3'],
            'Q4': item_dict['Q4'],
            'Q5': item_dict['Q5'],
            'Q6': item_dict['Q6'],
            'elapsedTime': item_dict['elapsedTime']
            }, 

        )
    # 처음 답변한다면 생성
    else:
        user = await db.user.create(
            {
            'userId': item_dict['userId'],
            'Q1': item_dict['Q1'],
            'Q2': item_dict['Q2'],
            'Q3': item_dict['Q3'],
            'Q4': item_dict['Q4'],
            'Q5': item_dict['Q5'],
            'Q6': item_dict['Q6'],
            'elapsedTime': item_dict['elapsedTime']
            }        
        )
    # if item_dict['id'] == 1:
    #     Q_list = Q_list1
    # elif item_dict['id'] == 2:
    #     Q_list = Q_list2
    # else:
    #     Q_list = Q_list3
    QR_list.append(f"주제 : {subject[0]}")
    QR_list.append(f"질문1. {Q_list1[0]}" + " | " + f"답변 : {R_list[item_dict['Q1']-1]}")
    QR_list.append(f"질문2. {Q_list1[1]}" + " | " + f"답변 : {R_list[item_dict['Q2']-1]}")
    QR_list.append(f"질문3. {Q_list1[2]}" + " |"  + f"답변 : {R_list[item_dict['Q3']-1]}")
    QR_list.append(f"질문4. {Q_list1[3]}" + " | " + f"답변 : {R_list[item_dict['Q4']-1]}")
    QR_list.append(f"질문5. {Q_list1[4]}" + " | " + f"답변 : {R_list[item_dict['Q5']-1]}")
    QR_list.append(f"질문6. {Q_list1[5]}" + " | " + f"답변 : {R_list[item_dict['Q6']-1]}")
    QR_list.append(f"총 설문 시간 : {item_dict['elapsedTime']}초")
    
    all_sentence = '\n'.join(QR_list)
    print(all_sentence)
    
    user = {
        f"Answer": send_survey_results(all_sentence)
        # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
        }        
    return user
#####################################################################

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port = 8000, reload=True)