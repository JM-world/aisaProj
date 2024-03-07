from langchain.prompts import ChatPromptTemplate, SystemMessagePromptTemplate, HumanMessagePromptTemplate
from langchain.schema import SystemMessage, HumanMessage
from langchain_openai import ChatOpenAI
import openai
import os
import json
from ast import literal_eval

# OpenAI API(유료, API 키 값은 보안을 위해 .env에 입력)
openai.api_key = os.getenv("OPENAI_API_KEY")

# DeepL API key(무료 버전, 월 50만 토큰까지 무료 번역) 
# import deepl
# deepl_key = "3d58744a-d5cd-fd8c-e078-015c393b54f4:fx"   # 무료 버젼

# deepl_key = os.getenv("DEEPL_AUTH_KEY")                   # 유료 버젼
# translator = deepl.Translator(deepl_key)                # Deepl 객체 생성

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
    "나는 친구의 기분을 이해하려고 노력한다.",
    "나는 친구의 마음(생각과 감정)을 잘 알 수 있다.",
    "나는 친구들의 고민을 잘 들어 준다.",
    "나는 친구가 선생님께 칭찬을 받으면 나도 기분이 좋아진다.",
    "나는 상대방의 표정과 몸짓을 살피면서 속마음을 이해한다.",
    "나는 대화할 때 이야기를 잘 듣고 있다는 것을 말이나 몸짓으로 보여준다.",
    "나는 대화를 할 때 어떻게 말할지 미리 생각하고 말한다.",
    "나는 듣는 사람이 이해할 수 있도록 쉽고 정확한 말을 골라 이야기 한다.",
    "나는 듣는 사람이 잘 이해할 수 있도록 예를 들어 설명한다."
]
# 창의적사고 능력 질문지
Q_list3 = [
    "나는 현재에 만족하지 않고 새로운 일에 도전하는 것을 좋아한다.",
    "나는 현실에서 일어날 수 없는 일을 자주 상상한다.",
    "나는 아무 생각 없이 같은 일을 계속 반복하는 것을 싫어한다.",
    "나는 친구들에 비해 질문이 많은 편이다.",
    "나는 그동안 경험하지 못한 일을 할 기회가 생기면 해보고 싶다.",
    "나는 궁금한 것을 못 참는다."
]

subject = ["다음 질문들은 설문자의 '비판적사고 능력'을 파악하는 질문들입니다.",
           "다음 질문들은 설문자의 '의사소통 능력'을 파악하는 질문들입니다.", 
           "다음 질문들은 설문자의 '창의적사고 능력'을 파악하는 질문들입니다." ]  # 큰 주제

# 설문에 따른 넛지 생성 함수
def send_survey_results(all_sentence, question_set):
    # OpenAI GPT-4 Turbo을 사용하여 챗봇 응답 생성
    # 챗봇 역할 지정

    if question_set == 1: # question 1~6 요약 > 넛지
        system_message_template = SystemMessagePromptTemplate.from_template(
            template=f"""우리는 설문조사 챗봇을 만들고 질문 리스트의 대한 설문지의 응답을 확인하고 답변이 불성실하다고 판단될 경우 AI nudge 기능을 추가해서 답변을 성실하게 할 수 있도록 멘트를 넣어줄 거야. 

[상황]
지금부터 너는 최고의 전문 설문 요원 40살 여자 Amy고, 설문에 답하는 친구는 15살 중학생이야. 

[설문 응답의 성실성 판단 기준]
설문에 답하는 친구의 성실성을 판단하는 기준은 아래 3가지야. 총 설문 시간과 답변의 균일 응답 정도, 답변의 상관 관계를 판단해서 위의 답변이 성실하게 진행되었는지 판단해줘.

총 설문 시간
총 설문 시간은 20초 정도 소요되는데, 총 설문 시간이 25초보다 짧다면 답변을 대충 읽고 넘어간 가능성이 높아. 이 경우에는 설문지를 성실하게 읽어달라고 부탁해줘.
총 설문 시간이 15초에서 35초 사이라면 신중하게 답변해줘서 고맙다고 말해줘.
총 설문 시간이 60초보다 크면 "설문지를 성실하게 응답하고 있는 거 맞지?"라고 물어봐줘.

답변의 균일 응답
모든 질문에 "전혀 그렇지 않다."로 한 가지로 응답하거나, "보통이다." 한 가지로 응답하는 식으로 1개 응답으로 균일하게 답변을 했다면 설문지를 보지 않고 찍었을 가능성이 높아. 이 부분에 주의를 줘야 해.

답변의 상관 관계
이 설문조사는 비판적 사고 능력에 관한 질문 내용으로 각 질문들의 상관 관계가 높기 때문에, 질문 1부터 6까지의 답변 분포가 "보통이다.", "그렇다.", "매우 그렇다."이거나 "전혀 그렇지 않다.", "그렇지 않다.", "보통이다."으로 되어 있어야 해. 만약 한 질문에서 "전혀 그렇지 않다."를 선택하고, 다른 질문에서 "매우 그렇다"를 선택했다면 일관성이 없는 답변을 한 것으로 판단하면 돼.

[Action]
너는 설문에 응답하는 청소년들의 답변을 확인하고 답변이 불성실하다고 판단될 경우, 답변에 다시 성실하게 임할 수 있도록 메시지를 전달하는 역할을 할거야. 메시지는 150자 이내로 반말로 작성해줘. 그리고 상황에 맞는 이모티콘도 넣어줘.응답자를 부를 때는 '친구야~!'라고 불러줘. 그리고 아래 판단 기준에 위배되는 내용은 간략하게 언급하되, 판단 기준을 상세하게 알려주면 안돼. 너는 최고의 설문 요원이니까 설문자가 즐겁게 너의 메시지를 읽고 성실하게 답변할 수 있도록 만들 수 있을거야. 만약 위 3가지 조건에 모두 해당하지 않는 답변이라면, 설문에 성실하게 임해줘서 고마워라는 메시지를 이모티콘과 함께 제공해줘.

                """ )
        
    elif question_set == 2: # question 7~15 요약 > 넛지
        system_message_template = SystemMessagePromptTemplate.from_template(
            template=f"""우리는 설문조사 챗봇을 만들고 질문 리스트의 대한 설문지의 응답을 확인하고 답변이 불성실하다고 판단될 경우 AI nudge 기능을 추가해서 답변을 성실하게 할 수 있도록 멘트를 넣어줄 거야. 

[상황]
지금부터 너는 최고의 전문 설문 요원 40살 여자 Amy고, 설문에 답하는 친구는 15살 중학생이야.

[설문 응답의 성실성 판단 기준]
설문에 답하는 친구의 성실성을 판단하는 기준은 아래 3가지야. 총 설문 시간과 답변의 균일 응답 정도, 답변의 상관 관계를 판단해서 설문의 답변이 성실하게 진행되었는지 판단해줘.

총 설문 시간
총 설문 시간은 20초 정도 소요되는데, 총 설문 시간이 60초보다 짧다면 답변을 대충 읽고 넘어간 가능성이 높아. 이 경우에는 설문지를 성실하게 읽어달라고 부탁해줘.
총 설문 시간이 15초에서 30초 사이라면 신중하게 답변해줘서 고맙다고 말해줘.
총 설문 시간이 60초보다 크면 "설문지를 성실하게 응답하고 있는 거 맞지?"라고 물어봐줘.

답변의 균일 응답
모든 질문에 "전혀 그렇지 않다."로 한 가지로 응답하거나, "보통이다." 한 가지로 응답하는 식으로 1개 응답으로 균일하게 답변을 했다면 설문지를 보지 않고 찍었을 가능성이 높아. 이 부분에 주의를 줘야 해.

답변의 상관 관계
이 설문조사는 의사소통 능력에 관한 질문 내용으로 각 질문들의 상관 관계가 높기 때문에, 답변 분포가 "보통이다.", "그렇다.", "매우 그렇다."이거나 "전혀 그렇지 않다.", "그렇지 않다.", "보통이다."으로 되어 있어야 해. 만약 한 질문에서 "전혀 그렇지 않다."를 선택하고, 다른 질문에서 "매우 그렇다"를 선택했다면 일관성이 없는 답변을 한 것으로 판단하면 돼.

[Action]
너는 설문에 응답하는 청소년들의 답변을 확인하고 답변이 불성실하다고 판단될 경우, 답변에 다시 성실하게 임할 수 있도록 메시지를 전달하는 역할을 할거야. 메시지는 150자 이내로 반말로 작성해줘. 그리고 상황에 맞는 이모티콘도 넣어줘. 응답자를 부를 때는 '친구야~!'라고 불러줘. 그리고 아래 판단 기준에 위배되는 내용은 간략하게 언급하되, 판단 기준을 상세하게 알려주면 안돼. 너는 최고의 설문 요원이니까 설문자가 즐겁게 너의 메시지를 읽고 성실하게 답변할 수 있도록 만들 수 있을거야. 만약 위 3가지 조건에 모두 해당하지 않는 답변이라면, 설문에 성실하게 임해줘서 고마워라는 메시지를 이모티콘과 함께 제공해줘.

            """ )
    else: # 전체요약 프롬프트 (question 1~21 전체 요약을 JAVA로 보내 줌.)
        system_message_template = SystemMessagePromptTemplate.from_template(
            template=f"""너는 최고의 전문 설문 진행 요원으로서, 너에게 설문 진행을 의뢰한 고객사에게 설문에 대한 답변이 진정성과 성실성을 담고 있어서 데이터로서 가치가 있는지 평가하고 고객사에게 설문 응답에 대한 평가 내용을 요약하여 전달할거야.

                     [상황]
                     설문 문항은 3개 영역(‘비판적 사고 능력’, ‘의사소통 능력’, ‘창의적 사고 능력’)으로 구성되어 있고, 총 21개 문항으로 구성되어 있어. 설문에 참여하는 사람들은 "전혀 그렇지 않다.", "그렇지 않다.", "보통이다.", "그렇다.", "매우 그렇다." 중에 1개를 답변으로 선택할거야.

                     [설문 문항]
                     ‘비판적 사고 능력’
                     질문1~6번

                     ‘의사소통 능력’
                     질문 7~15번 

                     ‘창의적 사고 능력’
                     질문 16~21번

                     [설문 응답의 성실성 판단 기준]
                     설문에 대한 응답의 진실성과 성실성을 판단하는 기준은 아래 3가지야. 총 설문 시간과 답변의 균일 응답 정도, 답변의 상관 관계를 판단해서 설문의 답변이 성실하게 진행되었는지 판단해줘.

                     총 설문 시간
                     각 영역별로 소요되는 설문 시간은 대략 아래와 같아. 만약 아래 기준 시간보다 약 20% 정도 더 소요되었다면 설문지에 집중하지 않고 응답한 것일 수 있고, 아래 기준 시간보다 약 20%정도 빠르게 응답했다면 설문지를 자세히 보지 않고 대충 답했을 가능성이 있어.

                     비판적 사고 능력: 약 20초
                     의사 소통 능력: 약 20초
                     창의적 사고 능력: 약 20초

                     답변의 균일 응답
                     모든 질문에 "전혀 그렇지 않다."로 한 가지로 응답하거나, "보통이다." 한 가지로 응답하는 식으로 1개 응답으로 균일하게 답변을 했다면 설문지를 보지 않고 한가지 응답으로 찍었을 가능성이 높아.

                     답변의 상관 관계
                     이 설문조사는 각 영역별로 구성된 문항의 상관 관계가 높기 때문에, 각 영역별로 답변 분포가 "보통이다.", "그렇다.", "매우 그렇다."이거나 "전혀 그렇지 않다.", "그렇지 않다.", "보통이다."으로 되어 있어야 해. 만약 한 질문에서 "전혀 그렇지 않다."를 선택하고, 다른 질문에서 "매우 그렇다"를 선택했다면 일관성이 없는 답변을 한 것으로 판단돼.

                     [Action]
                     너에게 설문 진행을 의뢰한 고객사에게 설문에 대한 답변의 진정성과 성실성에 대해 평가하고 요약 보고서와 설문 응답의 성실도를 1 혹은 2로 나눠서 전달 할거야. 

                     결과를 출력할 때 “result_message”와 “evaluation”이라는 2개의 key를 가진 JSON형식으로 할거야.
                     JASON의 키(Key)는 영어로 해줘.

                     “result_message” key에는 요약 내용을 넣어주고, 비판적 사고 능력, 의사소통 능력, 창의적 사고 능력 각각에 대한 평가를 요약하는데, 각 항목별로 메시지를 150자 이내로 작성해줘.
                     “evaluation” key에는 성실성이 있다고 판단하면 숫자 1을, 성실성이 없다고 판단하면 숫자 2를 출력해줘.

                     너는 최고의 설문 요원이니까 설문을 의뢰한 고객사에게 설문 응답에 대한 데이터로서의 가치를 정확하게 전달하고 데이터의 퀄리티 향상을 위해 어떠한 제안을 해서 설문 응답 데이터가 유용하게 활용될 수 있도록 도움을 줄 수 있을거야. 마지막 인사말에는 친근한 이모티콘도 추가해줘.

                     출력 값에 대한 예시는 아래를 참고하면 되고, 배열, 줄바꿈 기호 빼고 출력해줘.

                     [요약 예시]
                    "result_message : 아래와 같이 요약하여 내용을 고객사에게 전달드립니다." 
                    "1. 비판적 사고 능력: 설문 응답은 적절한 시간에 완료되었으며, 답변의 일관성과 분포가 보장되어 있어 진실성이 유지되고 있음을 확인했습니다. "
                    "2. 의사소통 능력: 설문 응답은 시간이 상당히 초과되었고, 답변의 일관성이 부족해 보입니다. 설문자가 집중하지 않고 응답한 것으로 보이니, 설문을 진행할 때 이 점을 고려해 주세요."
                    "3. 창의적 사고 능력: 설문 응답은 시간이 약간 초과되었으나, 답변의 분포와 일관성이 유지되고 있어 설문의 진실성이 보장됩니다."
                    "전체 요약: 다음 설문에서는 응답자가 더욱 집중하고 일관성 있게 응답하도록 유도하는 방법을 고려해 보시는 건 어떨까요? 이를 통해 데이터의 퀄리티를 높일 수 있을 것입니다. 감사합니다.",
                    "evaluation: 1"
                    
                    
                """ )

    # 사람 입력 데이터 양식
    human_message_template = HumanMessagePromptTemplate.from_template(template="{text}")

    # 챗봇 프롬프팅
    chat_prompt = ChatPromptTemplate.from_messages([system_message_template, human_message_template])

    # 모든 질문
    messages = chat_prompt.format_prompt(text=all_sentence).to_messages()
    messages_dict = []
    for message in messages:
        if isinstance(message, SystemMessage):
            messages_dict.append({"role": "system", "content": message.content})
        elif isinstance(message, HumanMessage):
            messages_dict.append({"role": "user", "content": message.content})
   
    # 챗봇 응답 생성 및 번역
    response = openai.chat.completions.create(
        model="gpt-4",
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
    print(chatbot_response)
    return chatbot_response

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
    Q1 : int | None = None           # 번호
    Q2 : int | None = None
    Q3 : int | None = None
    Q4 : int | None = None
    Q5 : int | None = None
    Q6 : int | None = None
    Q7 : int | None = None           # 번호
    Q8 : int | None = None
    Q9 : int | None = None
    Q10 : int | None = None
    Q11 : int | None = None
    Q12 : int | None = None
    Q13 : int | None = None          # 번호
    Q14 : int | None = None
    Q15 : int | None = None
    Q16 : int | None = None
    Q17 : int | None = None
    Q18 : int | None = None
    Q19 : int | None = None
    Q20 : int | None = None
    Q21 : int | None = None
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
    QR_list = [] # 질문 + 답변 값을 담을 리스트
    if found and item.Q1 is not None:
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
        
        ai_answer = {
            f"Answer": send_survey_results(all_sentence, 1)
            # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
            }        
        return ai_answer
    
    elif found and item.Q7 is not None:
        user = await db.user.update_many(
            where={
                    'userId': item_dict['userId']
                },
            data={
            'Q7': item_dict['Q7'],
            'Q8': item_dict['Q8'],
            'Q9': item_dict['Q9'],
            'Q10': item_dict['Q10'],
            'Q11': item_dict['Q11'],
            'Q12': item_dict['Q12'],
            'Q13': item_dict['Q13'],
            'Q14': item_dict['Q14'],
            'Q15': item_dict['Q15'],
            'elapsedTime': item_dict['elapsedTime'] + found.elapsedTime
            }, 
        )

        QR_list.append(f"주제 : {subject[1]}")
        QR_list.append(f"질문7. {Q_list2[0]}" + " | " + f"답변 : {R_list[item_dict['Q7']-1]}")
        QR_list.append(f"질문8. {Q_list2[1]}" + " | " + f"답변 : {R_list[item_dict['Q8']-1]}")
        QR_list.append(f"질문9. {Q_list2[2]}" + " |"  + f"답변 : {R_list[item_dict['Q9']-1]}")
        QR_list.append(f"질문10. {Q_list2[3]}" + " | " + f"답변 : {R_list[item_dict['Q10']-1]}")
        QR_list.append(f"질문11. {Q_list2[4]}" + " | " + f"답변 : {R_list[item_dict['Q11']-1]}")
        QR_list.append(f"질문12. {Q_list2[5]}" + " | " + f"답변 : {R_list[item_dict['Q12']-1]}")
        QR_list.append(f"질문13. {Q_list2[6]}" + " | " + f"답변 : {R_list[item_dict['Q13']-1]}")
        QR_list.append(f"질문14. {Q_list2[7]}" + " | " + f"답변 : {R_list[item_dict['Q14']-1]}")
        QR_list.append(f"질문15. {Q_list2[8]}" + " | " + f"답변 : {R_list[item_dict['Q15']-1]}")
        QR_list.append(f"총 설문 시간 : {item_dict['elapsedTime']}초")
    
        all_sentence = '\n'.join(QR_list)
        print(all_sentence)
        
        ai_answer = {
            f"Answer": send_survey_results(all_sentence, 2)
            # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
            }        
        return ai_answer
    
    elif found and item.Q16 is not None:
        await db.user.update_many(
            where={
                    'userId': item_dict['userId']
                },
            data={
            'Q16': item_dict['Q16'],
            'Q17': item_dict['Q17'],
            'Q18': item_dict['Q18'],
            'Q19': item_dict['Q19'],
            'Q20': item_dict['Q20'],
            'Q21': item_dict['Q21'],
            'elapsedTime': item_dict['elapsedTime'] + found.elapsedTime
            }, 
        )

        user_data = await db.user.find_many(where={'userId': item_dict['userId']})

        item_dict = user_data[0].model_dump()

        QR_list.append(f"주제 : {subject[0], subject[1], subject[2]}")
        QR_list.append(f"질문1. {Q_list1[0]}" + " | " + f"답변 : {R_list[item_dict['Q1']-1]}")
        QR_list.append(f"질문2. {Q_list1[1]}" + " | " + f"답변 : {R_list[item_dict['Q2']-1]}")
        QR_list.append(f"질문3. {Q_list1[2]}" + " |"  + f"답변 : {R_list[item_dict['Q3']-1]}")
        QR_list.append(f"질문4. {Q_list1[3]}" + " | " + f"답변 : {R_list[item_dict['Q4']-1]}")
        QR_list.append(f"질문5. {Q_list1[4]}" + " | " + f"답변 : {R_list[item_dict['Q5']-1]}")
        QR_list.append(f"질문6. {Q_list1[5]}" + " | " + f"답변 : {R_list[item_dict['Q6']-1]}")
        QR_list.append(f"질문7. {Q_list2[0]}" + " | " + f"답변 : {R_list[item_dict['Q7']-1]}")
        QR_list.append(f"질문8. {Q_list2[1]}" + " | " + f"답변 : {R_list[item_dict['Q8']-1]}")
        QR_list.append(f"질문9. {Q_list2[2]}" + " |"  + f"답변 : {R_list[item_dict['Q9']-1]}")
        QR_list.append(f"질문10. {Q_list2[3]}" + " | " + f"답변 : {R_list[item_dict['Q10']-1]}")
        QR_list.append(f"질문11. {Q_list2[4]}" + " | " + f"답변 : {R_list[item_dict['Q11']-1]}")
        QR_list.append(f"질문12. {Q_list2[5]}" + " | " + f"답변 : {R_list[item_dict['Q12']-1]}")
        QR_list.append(f"질문13. {Q_list2[6]}" + " | " + f"답변 : {R_list[item_dict['Q13']-1]}")
        QR_list.append(f"질문14. {Q_list2[7]}" + " | " + f"답변 : {R_list[item_dict['Q14']-1]}")
        QR_list.append(f"질문15. {Q_list2[8]}" + " | " + f"답변 : {R_list[item_dict['Q15']-1]}")
        QR_list.append(f"질문16. {Q_list3[0]}" + " | " + f"답변 : {R_list[item_dict['Q16']-1]}")
        QR_list.append(f"질문17. {Q_list3[1]}" + " |"  + f"답변 : {R_list[item_dict['Q17']-1]}")
        QR_list.append(f"질문18. {Q_list3[2]}" + " | " + f"답변 : {R_list[item_dict['Q18']-1]}")
        QR_list.append(f"질문19. {Q_list3[3]}" + " | " + f"답변 : {R_list[item_dict['Q19']-1]}")
        QR_list.append(f"질문20. {Q_list3[4]}" + " | " + f"답변 : {R_list[item_dict['Q20']-1]}")
        QR_list.append(f"질문21. {Q_list3[5]}" + " | " + f"답변 : {R_list[item_dict['Q21']-1]}")
        QR_list.append(f"총 설문 시간 : {item_dict['elapsedTime']}초")
    
        all_sentence = '\n'.join(QR_list)
        print(all_sentence)
        
        # print(send_survey_results(all_sentence, 3))
        print(type(send_survey_results(all_sentence, 3)))
        # print(literal_eval(send_survey_results(all_sentence, 3)))
        # print(literal_eval(send_survey_results(all_sentence, 3).replace('\n', '').replace("\\", ""))['evaluation'])

        result = literal_eval(send_survey_results(all_sentence, 3).replace('\n', '').replace("\\", ""))

        ai_answer = {
            f"Answer": result['result_message'],
            f"evaluation" : result['evaluation']
            # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
            }        
        print(ai_answer)
        
        return ai_answer

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
        
        ai_answer = {
            f"Answer": send_survey_results(all_sentence, 1)
            # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
            }        
        return ai_answer

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port = 8000, reload=True)