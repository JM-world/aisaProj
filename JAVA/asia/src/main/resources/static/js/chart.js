function yyyyMMdd(buttonValue) {
    var date = new Date();
    if (buttonValue === 'Today' || typeof buttonValue === 'undefined') {
        const weekDays = ['일', '월', '화', '수', '목', '금', '토'];


        const dayOfWeek = date.getDay();

        let result = [];

        for (let i = 6; i >= 0; i--) {
            let dayIndex = (dayOfWeek - i + 7) % 7;
            if (i === 0) {
                result.push('오늘');
            } else {
                result.push(weekDays[dayIndex]);
            }
        }

        return result;

    } else if (buttonValue === 'Week') {

        return ['6주', '5주', '4주', '3주', '2주', '1주 전', '이번 주'];

    } else {
        const months = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];

        const monthOfYear = date.getMonth();

        let result = [];

        for (let i = 6; i >= 0; i--) {
            let dayIndex = (monthOfYear - i + 12) % 12;
            if (i === 0) {
                result.push('이번 달');
            } else {
                result.push(months[dayIndex]);
            }
        }

        return result;

    }

}


function chart1(buttonValue) {
    console.log(buttonValue)
    var ct1 = document.getElementById('chart1');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct1.getAttribute('data-aaa-value'));
    new Chart(ct1, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                // 실제 데이터
                data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                // data: [22, 25, 25, 33, 35, 40, 45],
                borderWidth: 2,
                borderColor: 'rgba(255,0,0,0.5)',
                lineTension: 0,
                pointHoverRadius: 6,
                pointBorderColor: 'rgb(255,60,0)',
                pointBackgroundColor: 'rgb(255,255,255)',
            }]
        },
        options: {
            plugins: {
                legend: {
                    display: false,
                },
                title: {
                    display: false // Disable the title
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}

function chart2(buttonValue) {
    var ct2 = document.getElementById('chart2');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct2.getAttribute('data-aaa-value'));
    new Chart(ct2, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                // 실제 데이터
                data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                // data: [22, 12, 18, 22, 25, 29, 30],
                borderWidth: 2,
                borderColor: 'rgba(255,0,0,0.5)',
                lineTension: 0,
                pointHoverRadius: 6,
                pointBorderColor: 'rgb(255,60,0)',
                pointBackgroundColor: 'rgb(255,255,255)',
            }]
        },
        options: {
            plugins: {
                legend: {
                    display: false,
                },
                title: {
                    display: false // Disable the title
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}


function chart3(buttonValue) {
    var ct3 = document.getElementById('chart3');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct3.getAttribute('data-aaa-value'));
    new Chart(ct3, {
        type: 'doughnut',
        data: {
            labels: ["남자", "여자"],
            datasets: [{
                // 실제 데이터
                data: [data[0], data[1]],
                borderWidth: 2,
                borderColor: [
                    'rgb(124,124,124)',
                    'rgba(255,121,121)',
                ],
                backgroundColor: [
                    'rgba(124,124,124,0.5)',
                    'rgba(255,121,121,0.8)',
                ],
                lineTension: 0,
                pointHoverRadius: 6,
                pointBorderColor: 'rgba(255,0,0,0.5)',
                pointBackgroundColor: 'rgb(255,255,255)',
            }]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: false // 제목 끄기
                },
            },
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })

}

function chart4(buttonValue) {
    var ct4 = document.getElementById('chart4');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct4.getAttribute('data-aaa-value'));
    new Chart(ct4, {
        type: 'bar',
        data: {
            labels: ['중학생', '고등학생', '기타'],
            datasets: [{
                data: [data[0], data[1], data[2]],
                borderWidth: 2,
                borderColor: [
                    'rgb(255,162,162)',
                    'rgba(255,121,121)',
                    'rgb(124,124,124)'
                ],
                backgroundColor:[
                    'rgba(255,162,162,0.8)',
                    'rgba(255,121,121,0.8)',
                    'rgba(124,124,124,0.5)'
                ],
            }]
        },
        options: {
            plugins: {
                legend: {
                    display: false
                },
                title: {
                    display: false // 제목 끄기
                },
            },
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}

function chart5(buttonValue) {
    var ct5 = document.getElementById('chart5');

    var labels = yyyyMMdd(buttonValue);


    var avgBefore = JSON.parse(ct5.getAttribute('data-aaa-value'));
    var avgAfter = JSON.parse(ct5.getAttribute('data-bbb-value'));
    new Chart(ct5, {
        type: 'bar',
        data: {
            labels: ['1번', '2번', '3번', '4번', '5번', '6번'],
            datasets: [{
                label: '일반 설문 진행 시',
                data: [avgBefore[0], avgBefore[1], avgBefore[2], avgBefore[3], avgBefore[4], avgBefore[5]],
                borderWidth: 2,
                borderColor:
                    'rgb(124,124,124)'
                ,
                backgroundColor:
                    'rgba(124,124,124,0.8)',
            },
                {
                    data: [avgAfter[0], avgAfter[1], avgAfter[2], avgAfter[3], avgAfter[4], avgAfter[5]],
                    label: '성실 응답자 데이터',
                    borderWidth: 2,
                    borderColor:
                        'rgb(255,121,121)',
                    backgroundColor:
                        'rgba(255,121,121,0.8)',
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true, // y축 시작점을 0으로 설정
                    max: 5, // y축의 최대값을 명시적으로 5로 설정
                    ticks: {
                        stepSize: 1 // y축의 틱 간격을 1로 설정
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기/켜기
                    text: "비판적 사고 (6문항)",
                    color: 'rgb(66,75,255)',
                    font: {
                        size: 25,

                    }

                },

            },
            responsive: true,
            maintainAspectRatio: false,
        }
    })
}

function chart6(buttonValue) {
    var ct6 = document.getElementById('chart6');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct6.getAttribute('data-aaa-value'));
    new Chart(ct6, {
        type: 'polarArea',
        data: {
            labels: ['중학생', '고등학생', '기타'],
            datasets: [{
                label: '',
                data: [data[0], data[3], data[6]],
                borderWidth: 2,
                borderColor: [
                    'rgb(255,162,162)',
                    'rgba(255,121,121)',
                    'rgb(154,154,154)'
                    ],
                backgroundColor: [
                    'rgba(255,162,162,0.8)',
                    'rgba(255,121,121,0.8)',
                    'rgb(154,154,154,0.8)'
                    ]
            }]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기
                    text: "연령별 비교 (성실 응답자)",
                    color: 'rgb(255,121,121)',
                    font: {
                        size: 15
                    }
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}

function chart7(buttonValue) {
    var ct7 = document.getElementById('chart7');

    var labels = yyyyMMdd(buttonValue);


    var avgBefore = JSON.parse(ct7.getAttribute('data-aaa-value'));
    var avgAfter = JSON.parse(ct7.getAttribute('data-bbb-value'));
    new Chart(ct7, {
        type: 'bar',
        data: {
            labels: ['7번', '8번', '9번', '10번', '11번', '12번', '13번', '14번', '15번'],
            datasets: [{
                label: '일반 설문 진행 시',
                data: [avgBefore[6], avgBefore[7], avgBefore[8], avgBefore[9], avgBefore[10], avgBefore[11],
                    avgBefore[12], avgBefore[13], avgBefore[14]],
                borderWidth: 2,
                borderColor:
                    'rgb(124,124,124)'
                ,
                backgroundColor:
                    'rgba(124,124,124,0.8)',
            },
                {
                    // 실제 데이터
                    data: [avgAfter[6], avgAfter[7], avgAfter[8], avgAfter[9], avgAfter[10], avgAfter[11],
                        avgAfter[12], avgAfter[13], avgAfter[14]],
                    label: '성실 응답자 데이터',
                    borderWidth: 2,
                    borderColor:
                        'rgba(5,95,255)',
                    backgroundColor:
                        'rgba(65,95,255,0.8)',
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true, // y축 시작점을 0으로 설정
                    max: 5, // y축의 최대값을 명시적으로 5로 설정
                    ticks: {
                        stepSize: 1 // y축의 틱 간격을 1로 설정
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기/켜기
                    text: "의사소통 (9문항)",
                    color: 'rgb(255,1,1)',
                    font: {
                        size: 25,

                    }

                },

            },
            responsive: true,
            maintainAspectRatio: false,
        }
    })
}

function chart8(buttonValue) {
    var ct8 = document.getElementById('chart8');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct8.getAttribute('data-aaa-value'));
    new Chart(ct8, {
        type: 'polarArea',
        data: {
            labels: ['중학생', '고등학생', '기타'],
            datasets: [{
                label: '',
                data: [data[1], data[4], data[7]],
                borderWidth: 2,
                borderColor: [
                    'rgb(106,130,255)',
                    'rgba(65,95,255)',
                    'rgb(154,154,154)'
                ],
                backgroundColor: [
                    'rgb(106,130,255,0.8)',
                    'rgba(65,95,255,0.8)',
                    'rgb(154,154,154,0.8)'
                ]
            }]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기
                    text: "연령별 비교 (성실 응답자)",
                    color: 'rgb(51,83,253)',
                    font: {
                        size: 15
                    }
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}

function chart9(buttonValue) {
    var ct9 = document.getElementById('chart9');

    var labels = yyyyMMdd(buttonValue);


    var avgBefore = JSON.parse(ct9.getAttribute('data-aaa-value'));
    var avgAfter = JSON.parse(ct9.getAttribute('data-bbb-value'));
    new Chart(ct9, {
        type: 'bar',
        data: {
            labels: ['16번', '17번', '18번', '19번', '20번', '21번'],
            datasets: [{
                label: '일반 설문 진행 시',
                data: [avgBefore[15], avgBefore[16], avgBefore[17], avgBefore[18], avgBefore[19], avgBefore[20]],
                borderWidth: 2,
                borderColor:
                    'rgb(124,124,124)'
                ,
                backgroundColor:
                    'rgba(124,124,124,0.8)',
            },
                {
                    data: [avgAfter[15], avgAfter[16], avgAfter[17], avgAfter[18], avgAfter[19], avgAfter[20]],
                    label: '성실 응답자 데이터',
                    borderWidth: 2,
                    borderColor:
                        'rgba(89,178,26)',
                    backgroundColor:
                        'rgba(89,178,26,0.8)',
                }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true, // y축 시작점을 0으로 설정
                    max: 5, // y축의 최대값을 명시적으로 5로 설정
                    ticks: {
                        stepSize: 1 // y축의 틱 간격을 1로 설정
                    }
                }
            },
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기/켜기
                    text: "창의력 (6문항)",
                    color: 'rgb(66,75,255)',
                    font: {
                        size: 25,

                    }

                },

            },
            responsive: true,
            maintainAspectRatio: false,
        }
    })
}

function chart10(buttonValue) {
    var ct10 = document.getElementById('chart10');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct10.getAttribute('data-aaa-value'));
    new Chart(ct10, {
        type: 'polarArea',
        data: {
            labels: ['중학생', '고등학생', '기타'],
            datasets: [{
                label: '',
                data: [data[2], data[5], data[8]],
                borderWidth: 2,
                borderColor: [
                    'rgba(121,225,48)',
                    'rgba(102,197,35)',
                    'rgb(154,154,154)'
                ],
                backgroundColor: [
                    'rgba(121,225,48,0.8)',
                    'rgba(89,178,26,0.8)',
                    'rgba(154,154,154,0.8)'
                ]
            }]
        },
        options: {
            plugins: {
                legend: {
                    labels: {
                        color: 'rgb(154,154,154)',
                    }
                },
                title: {
                    display: true, // 제목 끄기
                    text: "연령별 비교 (성실 응답자)",
                    color: 'rgb(98,141,68)',
                    font: {
                        size: 15
                    }
                },
            },
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    display: false
                },
            }
        }
    })
}

if (document.getElementById('chart1')) {
    chart1();
}
if (document.getElementById('chart2')) {
    chart2();
}
if (document.getElementById('chart3')) {
    chart3();
}
if (document.getElementById('chart4')) {
    chart4();
}
if (document.getElementById('chart5')) {
    chart5();
}
if (document.getElementById('chart6')) {
    chart6();
}
if (document.getElementById('chart7')) {
    chart7();
}
if (document.getElementById('chart8')) {
    chart8();
}
if (document.getElementById('chart9')) {
    chart9();
}
if (document.getElementById('chart10')) {
    chart10();
}

