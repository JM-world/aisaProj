<script>
    import Step from "./Step.svelte";
    import { onMount } from 'svelte';
    // import WordCloud from "svelte-d3-cloud";

    onMount(() => {
        hljs.highlightAll(); // 모든 코드 블록을 강조 표시   
    });

    // const words = [
    // { text: "AI", count: 100 },
    // { text: "설문", count: 80 },
    // { text: "응답율", count: 50 },
    // { text: "웹/앱", count: 75 },
    // { text: "Nudge", count: 90 },
    // { text: "개인정보", count: 75 },
    // { text: "불성실 응답", count: 75 },
    // ];


    const securityAdmin = `@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				.requestMatchers(new AntPathRequestMatcher("/**")).permitAll());
		
		return http.build();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}`;

    const postMapping = `
@RequiredArgsConstructor
@Controller
public class MainController {
	private final QuestionRepository questionRepository;
	private final AnswerService answerService;
	
	// 첫번째 [시작화면]
	@GetMapping("/")
	public String main(HttpSession session) {
		session.invalidate();
		return "main";
	}
	....
	// 세번째 [설문화면]
	@GetMapping("/survey/{page}")
	public String getQuestionSetById(@PathVariable("page") int page, Model model) {
		// 시작 시간 체크
		stime = Instant.now();
		
	    List<Question> questionList = new ArrayList<>();
	    if (page == 1) {
	    	questionList = this.questionRepository.findByQuestionIdBetween(1, 6);
		} else if (page == 2) {
	        questionList = this.questionRepository.findByQuestionIdBetween(7, 15);
	    } else if (page == 3) {
	        questionList = this.questionRepository.findByQuestionIdBetween(16, 21);
	    }
	    
	    // 현재 사용자가 표시할 질문 세트를 모델에 추가합니다.
	    page += 1;
	    model.addAttribute("page", page);
	    model.addAttribute("questionList", questionList);
	    if (page == 4) {
	    	page = 2;
	    }
	    // survey.html은 질문 세트를 표시하는 템플릿 파일입니다.
	    return "survey";
	}
.....   
        // parameter setting
        String apiUrl = "http://192.168.0.50:8000/create/answer";
        URI uri = new URI(apiUrl);
        
        HashMap<String, Integer> body = new HashMap<>();
        body.put("userId", this.sessionId);
		
        for (int k = i; k <= j; k++) {
        	String answerString = answers.get("answers[" + k + "]");
    		body.put("Q" + k, Integer.parseInt(answerString));
    	}
	        
		body.put("elapsedTime", elapsedTime);
		
        // body.put("uid", uid); 등 정보를 계속 보낼 수 있음
        HttpEntity<HashMap<String, Integer>> entity = new HttpEntity<>(body, new HttpHeaders());

        // 정보를 주고 받음
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> response = rest.exchange(uri, HttpMethod.POST, entity, String.class);
	    JSONParser json = new JSONParser();
	    JSONObject obj = (JSONObject) json.parse(response.getBody().toString());
	    model.addAttribute("obj", obj.get("Answer").toString());
	    System.out.println(obj.get("Answer").toString());
	    this.obj = obj.get("Answer").toString();
	   ....
	}
}`

    const jpaMaria = `@RequiredArgsConstructor
@Service
public class AdminService {
	
	private final AdminRepository adminRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	public Admin create(String name, String password) {
		Admin admin = new Admin();
		admin.setName(name);
		admin.setPassword(passwordEncoder.encode(password));
		
		this.adminRepository.save(admin);
		return admin;
	}
}

public interface AdminRepository extends JpaRepository<Admin, Integer>{

}`;

    const svelteJava = `<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width,initial-scale=1" />
        <scrip t src="https://cdn.tailwindcss.com"></scrip t>
        <title>Svelte app</title>
        <link
            rel="stylesheet"
            href="../static/dist/build/bundle.css"
            th:href="@{/dist/build/bundle.css}"
        />
    </head>
    <body data-sveltekit-preload-data="hover" class="bg-slate-950 text-white relative">
        <div style="display: contents" class="relative z-[1]">
            <scrip t
                defer
                src="../static/dist/build/bundle.js"
                th:src="@{/dist/build/bundle.js}"
            ></scrip t>
        </div>
    </body>
</html>`;

    const fastApi = `# fast api 사용 모듈
from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware

# prisma 사용 모듈
import asyncio
from prisma import Prisma
from typing import Optional
from pydantic import BaseModel
    
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

    ai_answer = {
            f"Answer": send_survey_results(all_sentence, 1)
            # f"Q{ item_dict['id'] }의 답변 넛지": send_survey_results(all_sentence)
            }
    ...

    return ai_answer
    `

    const flutterSrc = `class QuestionRepository {

    static Future firstCreateQuestion({
        required List pointList,
        }) async {

        final dio = Dio();
        const targetUrl = 'http://192.168.0.50:8000/create/answer';

        final json = FirstQuestions(100, pointList[0], pointList[1], pointList[2], pointList[3],
            pointList[4], pointList[5], 20).toJSon();

        debugPrint(json.toString());

        final resp = await dio.post(targetUrl, data: json);

        debugPrint(resp.data['Answer']);

        return resp.data['Answer'];
        }
    }`

    const openAi = `from langchain.prompts import ChatPromptTemplate, SystemMessagePromptTemplate, HumanMessagePromptTemplate
from langchain.schema import SystemMessage, HumanMessage
from langchain_openai import ChatOpenAI
import openai
    
# 설문에 따른 넛지 생성 함수
def send_survey_results(all_sentence, question_set):
    # OpenAI GPT-3.5 Turbo을 사용하여 챗봇 응답 생성
    # 챗봇 역할 지정

    if question_set == 1: # question 1~6 요약 > 넛지
        system_message_template = SystemMessagePromptTemplate.from_template(
            template=f"""너는 최고의 전문 설문 요원 40살 여자 Amy고, 설문에 답하는 친구는 15살 중학생이야....""" )
        
    elif question_set == 2: # question 7~15 요약 > 넛지
       
    else: # 전체요약 프롬프트 (question 1~21 전체 요약을 JAVA로 보내 줌.)

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
`

    let steps = [
        { name: "공공데이터 | 설문 넛지", icon: "fa-solid fa-gavel", href: "#nudge"},
        { name: "JAVA Spring X Maria DB ", icon: "fa-brands fa-java", href: "#spring" },
        { name: "OpenAI API X Python", icon: "fa-brands fa-python", href: "#gpt4"},
    ];

    let benefits = [
        {
            name: "설문 내용",
            description:
                "공공데이터 포털 (DATA.go.kr)의 한국청소년활동진흥원 설문 문항 정보를 활용하였어요!",
        },
        {
            name: "OPEN AI API | GPT-4 X Nudge",
            description:
                "설문에 대한 내용을 확인하기 위하여 OPEN AI API | GPT-4를 활용하여 넛지를 줄 수 있어요. 설문자의 설문 의지를 향상시켜 설문 응답 성실도를 높일 수 있어요!",
        },
        {
            name: "친근한 UI, 넛지 말투",
            description:
                "설문자의 설문 의지를 향상시켜 설문 응답율을 높이기 위하여 청소년에 접근하기 쉬운 UI와 말투를 사용했어요! ",
        },
        {
            name: "불성실 응답 탐지",
            description:
                "아래 논문을 기반으로 하여 AI 프롬프팅을 진행하여 신뢰도 높은 넛지를 주기 위하여 노력했어요.!",
        },
    ];

    let surveys = [
                    {"category":"비판적사고(6문항)","question":"내가 한 행동이 나중에 어떤 결과를 가져올지 예상할 수 있다"},
                    {"category":"비판적사고(6문항)","question":"나는 어떤 일을 하고 있을 때 그 일의 부분들이 마지막 목표에 어떤 영향을 주는지 알 수 있다"},
                    {"category":"비판적사고(6문항)","question":"나는 다른 사람들이 말을 할 때 앞뒤가 맞는지를 따져본다"},
                    {"category":"비판적사고(6문항)","question":"나는 복잡하고 어려운 글을 읽고 이해하려고 애쓰는 것이 중요하다고 생각한다"},
                    {"category":"비판적사고(6문항)","question":"나는 어떤 사건에 대해 알맞은 근거를 찾아 객관적으로 판단하고 평가한다"},
                    {"category":"비판적사고(6문항)","question":"나는 어떤 의견에 대해서 결정할 때 다양한 방법을 생각하고 판단한다"},
                    {"category":"의사소통(9문항)","question":"나는 친구의 기분을 이해하려고 노력한다 "},
                    {"category":"의사소통(9문항)","question":"나는 친구의 마음(생각과 감정)을 잘 알 수 있다 "},
                    {"category":"의사소통(9문항)","question":"나는 친구들의 고민을 잘 들어 준다 "},
                    {"category":"의사소통(9문항)","question":"나는 친구가 선생님께 칭찬을 받으면 나도 기분이 좋아진다 "},
                    {"category":"의사소통(9문항)","question":"나는 상대방의 표정과 몸짓을 살피면서 속마음을 이해한다 "},
                    {"category":"의사소통(9문항)","question":"나는 대화할 때 이야기를 잘 듣고 있다는 것을 말이나 몸짓으로 보여준다 "},
                    {"category":"의사소통(9문항)","question":"나는 대화를 할 때 어떻게 말할지 미리 생각하고 말한다 "},
                    {"category":"의사소통(9문항)","question":"나는 듣는 사람이 이해할 수 있도록 쉽고 정확한 말을 골라 이야기 한다 "},
                    {"category":"의사소통(9문항)","question":"나는 듣는 사람이 잘 이해할 수 있도록 예를 들어 설명한다"},
                    {"category":"창의력(6문항)","question":"나는 현재에 만족하지 않고 새로운 일에 도전하는 것을 좋아한다 "},
                    {"category":"창의력(6문항)","question":"나는 현실에서 일어날 수 없는 일을 자주 상상한다 "},
                    {"category":"창의력(6문항)","question":"나는 아무생각 없이 같은 일을 계속 반복하는 것을 싫어한다 "},
                    {"category":"창의력(6문항)","question":"나는 친구들에 비해 질문이 많은 편이다"},
                    {"category":"창의력(6문항)","question":"나는 그동안 경험하지 못한 일을 할 기회가 생기면 해보고 싶다"},
                    {"category":"창의력(6문항)","question":"나는 궁금한 것을 못 참는다"}
                ];
    
    let springMarias = [
                                {
                                    name: "Spring Framework",
                                    description:
                                        "Spring Framework를 사용하였어요. Spring Security를 사용하여 관리자만 Admin에 접속할 수 있어요!. Http 통신으로 FAST API 서버와 접속하여 넛지 데이터를 가져올 수 있어요!",
                                },
                                {
                                    name: "Maria DB, JPA, Svelte",
                                    description:
                                        "Maria DB를 데이터베이스로 하여 JPA를 이용한 통신을 할 수 있어요! API 통신을 통하여 Python AI Server와 접속할 수 있어요! Landing Page를Svelte로 만들었어요!",
                                },
                                {
                                    name: "Flutter X Android app Mockup",
                                    description:
                                        "Flutter를 통하여 Mockup 형태로 Android app으로 Porting을 진행하였어요!.",
                                },
                            ];

    let openPythons = [
                            {
                                name: "FAST API",
                                description:
                                    "스프링과의 통신을 위하여 FAST API를 사용하여 AI Server를 만들었어요.",
                            },
                            {
                                name: "Lang Chain",
                                description:
                                    "Lang Chain으로 OPEN AI | GPT-4가 설문 내용을 감지할 수 있도록 했어요. OPEN AI | GPT-4에게서 받은 넛지를 다시 Spring 서버에 전달할 수 있어요.",
                            },
                        ];
    let lessons = [
                            {
                                name: "REST API 활용",
                                description:
                                    "JAVA팀과 Python기반으로 학습한 인원들이 서로 협력하여 REST API 기반으로 통신할 수 있는 기술을 배웠어요!",
                            },
                            {
                                name: "Lang Chain 활용",
                                description:
                                "Lang Chain을 이용하여 설문내용에 대하여 GPT에게 분석할 수 있도록 하는 프롬프팅 기술을 배울 수 있는 계기가 되었어요!",
                            },
                            {
                                name: "AI Nudge 활용",
                                description:
                                    "OPEN AI API를 이용하여 설문 중간중간마다 AI Nudge를 줄 수 있고 응답 품질 상승을 기대할 수 있을 것 같아요!",
                            },
                            {
                                name: "사용자 친화적 UI/UX",
                                description:
                                    "고객 수요에 맞는 디자인 요소를 고민하는 등 여러가지 시장 수요에 적합한 프로덕트를 만들 수 있는 계기가 되었어요.",
                            },
                        ];
</script>

<main class="flex flex-col flex-1 p-4">
    <section
        id="introPage"
        class="grid grid-cols-1 lg:grid-cols-2 gap-10 py-8 sm:py-14"
    >
        <div
            class="flex flex-col lg:justify-center text-center lg:text-left gap-6 md:gap-8 lg:gap-8"
        >
            <h2 class="font-semibold text-4xl sm:text-5xl md:text-6xl">
                <span class="poppins text-violet-400">AI</span> 를 활용한 설문조사
                
            </h2>
            <span class="sunflower fontSignature text-7xl">&#35; 설문해 U</span>
            <p class="text-base sm:text-lg md:text-xl">
                청소년 활동 참여 청소년을 대상으로 진행할 수 있는 <br /><span class="text-orange-300">3대 역량(비판적 사고, 의사소통, 창의력)</span>에 대한 설문입니다.
                
            </p>
            <p class="text-base sm:text-lg md:text-xl">아래 <span class="text-violet-400">버튼</span> 을 눌러 설문을 시작 해주세요.</p>
            <a
                class="blueShadow mx-auto lg:mr-auto lg:ml-0 text-base sm:text-lg md:text-xl poppins
            relative overflow-hidden px-6 py-3 group rounded-full bg-white text-slate-950
            cursor-pointer"
                href="/main"
            >
                <div
                    class="absolute top-0 right-full w-full h-full bg-violet-400 opacity-20 group-hover:translate-x-full z-0 duration-200"
                ></div>
                <h4 class="notokr relative z-9">설문으로 가기&rarr;</h4>
            </a>
        </div>
        <div class="relative shadow-2xl grid place-items-center">
            <img
                src={"images/holeman.png"}
                alt="Profile image"
                class="object-scale-down z-[2] max-h-[70vh]"
            />
        </div>
    </section>
    <section id="projects" class="py-20 lg:py-32 flex flex-col gap-24">
            <div class="flex flex-col gap-2 text-center">
                <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pb-5">
                    <span class="poppins text-violet-400">주제 선정 배경</span>
                </h3>
                <div class="mt-11 grid lg:grid-cols-6 lg:gap-10">
                    <div
                    class="lg:col-start-2 lg:col-span-4 p-4 sm:p-6 md:p-8 flex flex-col
                    gap-4 rounded-lg border border-solid border-violet-700 text-center group
                    cursor-pointer hover:border-violet-400 duration-200"
                    >
                    <div
                    class="bg-slate-950 grid place-items-center px-4 text-5xl md:text-6xl
                    -mt-10 sm:-mt-12 md:-mt-14 lg:-mt-16 mx-auto duration-200"
                    >
                    <i class="fa-solid fa-object-ungroup"></i>
                    </div>
                    <div class="relative shadow-2xl grid place-items-center rounded-2xl border-2 border-violet-700">
                        <!-- <WordCloud font={'Noto Sans KR'} words={words} backgroundColor=#4d377b/> -->
                        <img
                        src={"images/wordcloud2.jpg"}
                        alt="Profile image"
                        class="object-scale-down z-[2] max-h-[70vh] rounded-2xl"
                    />
                    </div>
                </div>
            </div>
        </div>
        </section>
    <section id="projects" class="py-20 lg:py-32 flex flex-col gap-24">
        <div class="flex flex-col gap-2 text-center">
            <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl">
                프로젝트는 아래<span class="poppins text-violet-400">&nbsp;3단계</span>로 구성 되었어요!
            </h3>
            <a
                href="https://github.com/JM-world/aisaProj"
                target="_blank"
                class="mx-auto px-4 py-2 rounded-md border
            border-solid border-white flex items-center gap-2 mb-4 sm:mb-0
            mt-10 hover:border-violet-700 duration-200"
            >
                <i class="fa-regular fa-circle-play"></i>
                <p>GitHub에서 확인해 보기</p>
            </a>
            <div class="mt-11 grid grid-cols-1 lg:grid-cols-3 gap-12 lg:gap-10">
                <Step step={steps[0]}>
                    <div class="text-left"> &#35; 본 데이터는 공공데이터 포털(한국청소년활동진흥원)에서 제공하는 데이터로 청소년 활동 참여 청소년을 대상으로 진행할 수 있는
                        <strong class="fontSignature"
                            >3대 역량(비판적 사고, 의사소통, 창의력)
                        </strong>에 대한 설문 문항 정보를 이용하였어요.<br />
                        &#35; <strong class="fontSignature">OPEN AI | GPT-4</strong>에게서 받은 <strong class="fontSignature">넛지</strong>
                        를 이용하여 설문자의 설문 의지를 향상시킬 수 있어요!
                    </div>
                </Step>
                <Step step={steps[1]}>
                    <div class="text-left">
                        &#35; 웹 통신용<strong class="fontSignature">Spring Framework</strong>를 사용하였어요.
                        <br />
                        &#35;<strong class="fontSignature"
                            >&nbsp;Spring Security</strong>를 사용하여 관리자만 Admin에 접속할 수 있어요!
                        <br />
                        &#35;&nbsp;<strong class="fontSignature">Maria DB</strong>를 데이터베이스로 하여 
                        &#35;&nbsp;<strong class="fontSignature">JPA</strong>를 이용한 통신을 할 수 있어요!
                        <br />
                        &#35;&nbsp;<strong class="fontSignature">API 통신</strong>을 통하여 Python AI Server와 접속할 수 있어요!
                        <br />
                        &#35; Landing Page를&nbsp;<strong class="fontSignature">Svelte</strong>로 만들었어요!
                    </div>
                </Step>
                <Step step={steps[2]}>
                    <div class="text-left">
                        &#35; 스프링과의 통신을 위하여 <strong class="fontSignature">FAST API</strong>를 사용하여 AI Server를 만들었어요.
                        <br />
                        &#35;<strong class="fontSignature"
                            >&nbsp;Lang Chain</strong>으로 <strong class="fontSignature">OPEN AI | GPT-4</strong>가 설문 내용을 감지할 수 있도록 했어요.
                        <br />
                        &#35;&nbsp;<strong class="fontSignature">OPEN AI | GPT-4</strong>에게서 받은 <strong class="fontSignature">넛지</strong>를 다시 Spring 서버에 전달할 수 있어요.
                    </div>
                </Step>
            </div>
        </div>
    </section>
    <section id="projects" class="py-20 lg:py-32 flex flex-col gap-24">
        <div class="flex flex-col gap-2 text-center">
            <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pb-5">
                <span class="poppins text-violet-400">System Architecture</span>
            </h3>
            <div class="mt-11 grid lg:grid-cols-6 lg:gap-10">
                <div
                class="lg:col-start-2 lg:col-span-4 p-4 sm:p-6 md:p-8 flex flex-col
                gap-4 rounded-lg border border-solid border-violet-700 text-center group
                cursor-pointer hover:border-violet-400 duration-200"
                >
                <div
                class="bg-slate-950 grid place-items-center px-4 text-5xl md:text-6xl
                -mt-10 sm:-mt-12 md:-mt-14 lg:-mt-16 mx-auto duration-200"
                >
                <i class="fa-solid fa-diagram-project"></i>
                </div>
                <div class="relative shadow-2xl grid place-items-center rounded-2xl border-2 border-violet-700">
                    <img
                        src={"images/systemArchitecture.png"}
                        alt="Profile image"
                        class="object-scale-down z-[2] max-h-[70vh] rounded-2xl"
                    />
                </div>
            </div>
        </div>
    </div>
    </section>
    <section
        id="nudge"
        class="py-20 pt-10 lg:py-32 flex flex-col gap-16 sm:gap-20 md:gap-24 relative"
    >
        <div
            class="flex flex-col gap-2 text-center relative before:relative before:top-0
        before:left-0 before:w-2/3 before:h-1.5 before:bg-violet-700 after:absolute after:bottom-0
        after:right-0 after:w-2/3 after:h-1.5 after:bg-violet-700 py-4"
        >
            <h6 class="text-lg sm:text-xl md:text-2xl"><strong class="text-violet-400">공공데이터</strong>를 기반한 <strong class="text-violet-400">청소년</strong> 대상 설문</h6>
            <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl">
                <span class="poppins text-violet-400">넛지</span>를 이용한 설문 응답 품질 향상!!
            </h3>
        </div>
        
        <div class="flex flex-col gap-20 w-full mx-auto max-w-[800px]">
            {#each benefits as benefit, index}
                <div class="flex gap-6 sm:gap-8">
                    <p
                        class="poppins text-4xl sm:text-5xl md:text-6xl text-slate-500
                    font-semibold"
                    >
                        {index + 1}
                    </p>
                    <div class="flex flex-col gap-6 sm:gap-8">
                        <h5 class="text-2xl sm:text-3xl md:text-5xl">
                            {benefit.name}
                        </h5>
                        <p class="text-sm sm:text-sm md:text-lg">{benefit.description}</p>
                        {#if index == 1}
                        <div class="relative shadow-2xl grid place-items-center">
                            <img
                                src={"images/nudgeBook01.jpg"}
                                alt="Profile image"
                                class="object-scale-down z-[2] max-h-[70vh]"
                            />
                        </div>
                        {/if}
                        {#if index == 0}
                        <div class="flex flex-col gap-10 max-w-[800px] mx-auto w-full rounded-2xl border-2 border-violet-700">
                            <table class="table-auto bg-white text-slate-700 border-2 border-violet-700 text-center">
                                <caption class="caption-top text-slate-200 pt-4 pb-4">
                                    Table 1: 한국청소년활동진흥원_설문 문항 정보_20221231
                                </caption>
                                <caption class="caption-bottom text-slate-200 pt-2 pb-2 text-right">
                                    출처:
                                    <a class="hover:text-violet-300" 
                                    href="https://www.data.go.kr/data/15121201/fileData.do#tab-layer-openapi" 
                                    target="_blank">공공데이터 포털(DATA.go.kr)&nbsp;</a>
                                </caption>
                                <thead class="border-b border-solid border-slate-200">
                                    <tr>
                                        <th class="whitespace-nowrap bg-violet-700 p-2 px-4 text-white">No</th>
                                        <th class="whitespace-nowrap bg-violet-700 p-2 px-4 text-white ">구분</th>
                                        <th class="whitespace-nowrap bg-violet-700 p-4 px-8 text-white">
                                            질문
                                        </th>
                                    </tr>
                                </thead>
                                <tbody class="text-left">
                                    {#each surveys.slice(0, 6) as survey, index}
                                        <tr class="border-b border-solid border-slate-200">
                                            <td class="text-center">{index + 1}</td>
                                            {#if index == 0}
                                                <td rowspan="6"
                                                    class="border-r border-solid border-white pl-4 pr-8 py-4 font-semibold
                                                text-sm bg-slate-100 text-center">{surveys[0].category.slice(0,3)}<br />{surveys[0].category.slice(3,5)}<br />{surveys[0].category.slice(5)}</td
                                                >
                                            {/if}
                                            
                                                <td>{survey.question}</td
                                                >
                                            </tr>
                                    {/each}
                                    {#each surveys.slice(6, 15) as survey, index}
                                        <tr class="border-b border-solid border-slate-200">
                                            <td class="text-center">{index + 7}</td>
                                            {#if index == 0}
                                                <td rowspan="9"
                                                    class="border-r border-solid border-white pl-4 pr-8 py-4 font-semibold
                                                text-sm bg-slate-100 text-center">{surveys[6].category.slice(0,2)}<br />{surveys[6].category.slice(2,4)}<br />{surveys[6].category.slice(4)}</td
                                                >
                                            {/if}
                                                <td>{survey.question}</td
                                                >
                                        </tr>
                                    {/each}
                                    {#each surveys.slice(15, 21) as survey, index}
                                        <tr class="border-b border-solid border-slate-200">
                                            <td class="text-center">{index + 16}</td>
                                            {#if index == 0}
                                                <td rowspan="9"
                                                    class="border-r border-solid border-white pl-4 pr-8 py-4 font-semibold
                                                text-sm bg-slate-100 text-center">{surveys[15].category.slice(0,3)}<br />{surveys[15].category.slice(3)}</td
                                                >
                                            {/if}
                                                <td>{survey.question}</td
                                                >
                                        </tr>
                                    {/each}
                                </tbody>
                            </table>
                        </div>
                    {/if}
                    {#if index == 3}
                    <div class="flex flex-col gap-10 max-w-[800px] mx-auto w-full rounded-2xl border-2 border-violet-700">
                        <table class="table-auto bg-white text-slate-700 border-2 border-violet-700 text-center">
                            <caption class="caption-top text-slate-200 pt-4 pb-4">
                                Table 2: 불성실 응답의 유형별 분류 의미 및 예시
                            </caption>
                            <caption class="caption-bottom text-slate-200 pt-2 pb-2 text-right">
                                출처:
                                <a class="hover:text-violet-300" 
                                href="http://kmr.kasba.or.kr/xml/25318/25318.pdf" 
                                target="_blank">Korean Management Review(http://kmr.kasba.or.kr)&nbsp;</a>
                            </caption>
                            <tbody class="text-center">
                                <tr class="border-b border-solid border-slate-200">
                                    <td class="text-center flex justify-center">
                                        <img
                                        src={"images/responding.png"}
                                        alt="Profile image"
                                        class="object-scale-down z-[2] max-h-[70vh]"
                                    />
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    {/if}
                    </div>
                </div>
            {/each}
        </div>
        
    </section>
    <section
        id="spring"
        class="py-20 pt-10 lg:py-32 flex flex-col gap-16 sm:gap-20 md:gap-24 relative"
    >
        <div
            class="flex flex-col gap-2 text-center relative before:relative before:top-0
        before:left-0 before:w-2/3 before:h-1.5 before:bg-violet-700 after:absolute after:bottom-0
        after:right-0 after:w-2/3 after:h-1.5 after:bg-violet-700 py-4"
        >
            <div class="flex flex-row justify-center">
                <div>
                    <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pt-4 pb-4">
                        <span class="poppins text-violet-400">Spring</span>을 기반한 <strong class="text-violet-400">WAS</strong>
                    </h3>
                </div>
                <div class="hidden sm:block">
                    <button
                    class="ml-auto rounded-full aspect-square bg-slate-300
                    text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                    >
                        <img class="h-7" src="images/svelte.svg">
                    </button>
                    <button
                    class="ml-auto rounded-full aspect-square bg-cyan-400
                    text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                    >
                        <img class="h-7" src="images/tailwindcss.svg">
                    </button>
                    <button
                    class="ml-auto rounded-full aspect-square bg-green-500
                    text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                    >
                        <img class="h-7" src="images/springboot.svg">
                    </button>
                    <button
                    class="ml-auto rounded-full aspect-square bg-blue-500
                    text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                    >
                        <img class="h-7" src="images/mariadbfoundation.svg">
                    </button>
                    <button
                        class="ml-auto rounded-full aspect-square bg-indigo-300
                        text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                        >
                            <img class="h-7" src="images/flutter.svg">
                        </button>
                        <button
                        class="ml-auto rounded-full aspect-square bg-green-500
                        text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                        >
                        <img class="h-7" src="images/android.svg">
                    </button>
                </div>
            </div>
        </div>
        
        <div class="flex flex-col gap-20 w-full mx-auto max-w-[800px]">
            {#each springMarias as maria, index}
                <div class="flex gap-6 sm:gap-8">
                    <p
                        class="poppins text-4xl sm:text-5xl md:text-6xl text-slate-500
                    font-semibold"
                    >
                        {index + 1}
                    </p>
                    <div class="flex flex-col gap-6 sm:gap-8">
                        <h3 class="text-2xl sm:text-3xl md:text-5xl">
                            {maria.name}
                        </h3>
                        <p class="text-sm sm:text-sm md:text-lg">{maria.description}</p>
                    </div>
                </div>
                {#if index == 0}
                    <pre><h5>SecurityConfig.java</h5><code class="language-java">{securityAdmin}</code></pre>
                    <pre><h5>MainController.java</h5><code class="language-java">{postMapping}</code></pre>
                {/if}
                {#if index == 1}
                    <pre><h5>AdminService.java</h5><code class="language-java">{jpaMaria}</code></pre>
                    <div>
                        <h5>Spring folder</h5>
                        <img  src={"images/svelte.png"}
                            alt="Profile image"
                            class="object-cover z-[2] max-h-[70vh]" />
                    </div>
                <pre><h5>index.html</h5><code class="language-html">{svelteJava}</code></pre>
                    
                {/if}
                {#if index == 2}
                    <pre><h5>question_repository.dart</h5><code class="language-java">{flutterSrc}</code></pre>
                {/if}
            {/each}
        </div>
    </section>

    <section
        id="gpt4"
        class="py-20 pt-10 lg:py-32 flex flex-col gap-16 sm:gap-20 md:gap-24 relative"
    >
        <div
            class="flex flex-col gap-2 text-center relative before:relative before:top-0
        before:left-0 before:w-2/3 before:h-1.5 before:bg-violet-700 after:absolute after:bottom-0
        after:right-0 after:w-2/3 after:h-1.5 after:bg-violet-700 py-4"
        >
            <div class="flex justify-center">
                <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pb-4 pt-4">
                    <span class="poppins text-violet-400">Fast API</span>를 이용한 OPEN AI API와 Spring 서버 연결!
                </h3>
            </div>
            <div class="hidden sm:block">
                <button
                class="ml-auto rounded-full aspect-square bg-emerald-700
                text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                >
                    <img class="h-7" src="images/fastapi.svg">
                </button>
                <button
                class="ml-auto rounded-full aspect-square bg-sky-300
                text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                >
                    <img class="h-7" src="images/openai.svg">
                </button>
                <button
                class="ml-auto rounded-full aspect-square bg-sky-500
                text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                >
                    <img class="h-7" src="images/python.svg">
                </button>
                <button
                class="ml-auto rounded-full aspect-square bg-blue-500
                text-violet-400 sm:px-3 sm:ms-1 hover:bg-slate-800 cursor-pointer"
                >
                    <img class="h-7" src="images/mariadbfoundation.svg">
                </button>
            </div>
        </div>
        
        <div class="flex flex-col gap-20 w-full mx-auto max-w-[800px]">
            {#each openPythons as python, index}
                <div class="flex gap-6 sm:gap-8">
                    <p
                        class="poppins text-4xl sm:text-5xl md:text-6xl text-slate-500
                    font-semibold"
                    >
                        {index + 1}
                    </p>
                    <div class="flex flex-col gap-6 sm:gap-8">
                        <h3 class="text-2xl sm:text-3xl md:text-5xl">
                            {python.name}
                        </h3>
                        <p class="text-sm sm:text-sm md:text-lg">{python.description}</p>
                    </div>
                </div>
                {#if index == 0}
                    <pre><h5>main.py</h5><code class="language-python">{fastApi}</code></pre>
                {/if}
                {#if index == 1}
                    <pre><h5>main.py</h5><code class="language-python">{openAi}</code></pre>
                {/if}
            {/each}
        </div>
    </section>
    <section
        id="lessonLearned"
        class="py-20 pt-10 lg:py-32 flex flex-col gap-16 sm:gap-20 md:gap-24 relative"
    >
        <div
            class="flex flex-col gap-2 text-center relative before:relative before:top-0
        before:left-0 before:w-2/3 before:h-1.5 before:bg-violet-700 after:absolute after:bottom-0
        after:right-0 after:w-2/3 after:h-1.5 after:bg-violet-700 py-4"
        >
            <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pb-4 pt-4">
                <span class="poppins text-violet-400">Lesson Learned</span>
            </h3>
        </div>
        
        <div class="flex flex-col gap-20 w-full mx-auto max-w-[800px]">
            {#each lessons as lesson, index}
                <div class="flex gap-6 sm:gap-8">
                    <p
                        class="poppins text-4xl sm:text-5xl md:text-6xl text-slate-500
                    font-semibold"
                    >
                        {index + 1}
                    </p>
                    <div class="flex flex-col gap-6 sm:gap-8">
                        <h3 class="text-2xl sm:text-3xl md:text-5xl">
                            {lesson.name}
                        </h3>
                        <p class="text-sm sm:text-sm md:text-lg">{lesson.description}</p>
                    </div>
                </div>
            {/each}
        </div>
    </section>

    <section id="techStack" class="py-20 lg:py-32 flex flex-col gap-24">
        <div
        class="flex flex-col gap-2 text-center relative before:relative before:top-0
        before:left-0 before:w-2/3 before:h-1.5 before:bg-violet-700 after:absolute after:bottom-0
        after:right-0 after:w-2/3 after:h-1.5 after:bg-violet-700 py-4"
        >
            <h3 class="font-semibold text-3xl sm:text-4xl md:text-5xl pb-4 pt-4">
                <span class="poppins text-violet-400">기술 Stack</span>
            </h3>
        </div>
        <div class="mt-11 grid lg:grid-cols-6 lg:gap-10">
            <div
            class="lg:col-start-2 lg:col-span-4 p-4 sm:p-6 md:p-8 flex flex-col
            gap-4 rounded-lg border border-solid border-violet-700 text-center group
            cursor-pointer hover:border-violet-400 duration-200"
            >
                <div
                class="bg-slate-950 grid place-items-center px-4 text-5xl md:text-6xl
                -mt-10 sm:-mt-12 md:-mt-14 lg:-mt-16 mx-auto duration-200"
                >
                <i class="fa-solid fa-chalkboard-user"></i>
                </div>
                <div class="flex flex-col">
                    <div class="flex flex-row justify-center">
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Svelte-FF3E00?style=for-the-badge&logo=Svelte&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/FastAPI-000000?style=for-the-badge&logo=FastAPI&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Tailwindcss-79EDFF?style=for-the-badge&logo=tailwindcss&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=Chart.js&logoColor=white">
                        </div>

                    </div>
                    <div class="flex flex-row justify-center">
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=Python&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/MariaDB-F80000?style=for-the-badge&logo=mariadb&logoColor=white">
                        </div>

                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=Thymeleaf&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jQuery&logoColor=white">
                        </div>


                    </div>
                    <div class="flex flex-row justify-center">
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/OpenAI-412991?style=for-the-badge&logo=OpenAI&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=Bootstrap&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=HTML5&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=CSS3&logoColor=white">
                        </div>

                    </div>
                    <div class="flex flex-row justify-center">
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Javascript-F7DF1E?style=for-the-badge&logo=Javascript&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Font Awesome-528DD7?style=for-the-badge&logo=Font Awesome&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=Git&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white">
                        </div>
                        <div>
                            <img class="h-10" src="https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=Discord&logoColor=white">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main>
