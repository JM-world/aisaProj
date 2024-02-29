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
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data: [22, 25, 25, 33, 35, 40, 45],
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
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data: [22, 12, 18, 22, 25, 29, 30],
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
        type: 'bar',
        data: {
            labels: ['남자', '여자'],
            datasets: [{
                // 실제 데이터
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data: [22, 12],
                borderWidth: 2,
                borderColor: [
                    'rgba(40,58,255, 0.8)',
                    'rgb(255,60,0)',
                ],
                backgroundColor: [
                    'rgba(40,58,255, 0.2)',
                    'rgba(255,60,0, 0.3)',
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

function chart4(buttonValue) {
    var ct4 = document.getElementById('chart4');

    var labels = yyyyMMdd(buttonValue);


    var data = JSON.parse(ct4.getAttribute('data-aaa-value'));
    new Chart(ct4, {
        type: 'bar',
        data: {
            labels: ['10대 미만', '10대', '20대', '30대 이상'],
            datasets: [{
                // 실제 데이터
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data: [22, 55, 11, 5],
                borderWidth: 2,
                borderColor: 'rgb(255,60,0)',
                backgroundColor:
                    'rgba(255,60,0, 0.3)',
                lineTension: 0,
                pointHoverRadius: 6,
                pointBorderColor: 'rgba(255,0,0,0.5)',
                pointBackgroundColor: 'rgb(255,255,255)',
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


    var data = JSON.parse(ct5.getAttribute('data-aaa-value'));
    new Chart(ct5, {
        type: 'bar',
        data: {
            labels: ['1번', '2번', '3번', '4번', '5번', '6번'],
            datasets: [{
                label: '일반 설문 진행 시',
                // 실제 데이터
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data:
                    [2.1, 3.6, 1.9, 2.1, 3.6, 1.9,
                        2.1, 3.6, 1.9, 2.1, 3.6, 1.9, 2.1, 3.6, 1.9,
                        2.1, 3.6, 1.9, 2.1, 3.6, 1.9],
                borderWidth: 2,
                borderColor:
                    'rgb(124,124,124)'
                ,
                backgroundColor:
                    'rgba(129,129,129,0.8)',
            },
                {
                    // 실제 데이터
                    // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                    // 가상 데이터
                    label: '성실 응답자 데이터',
                    data:
                        [4.1, 4.6, 3.9, 4.1, 4.6, 3.9,
                            4.1, 4.6, 3.9, 4.1, 4.6, 3.9, 4.1, 4.6, 3.9,
                            4.1, 4.6, 3.9, 4.1, 4.6, 3.9,],
                    borderWidth: 2,
                    borderColor:
                        'rgb(255,121,121)',
                    backgroundColor:
                        'rgba(255,121,121,0.8)',
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
                    display: true, // 제목 끄기/켜기
                    text: "비판적 사고 (6문항)",
                    color: 'rgb(255,255,255)',
                    font: {
                        size: 15
                    }

                },

            },
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
            labels: ['10대 미만', '10대', '20대', '30대 이상'],
            datasets: [{
                label: '',
                // 실제 데이터
                // data: [data[6], data[5], data[4], data[3], data[2], data[1], data[0]],
                // 가상 데이터
                data: [22, 55, 11, 5],
                borderWidth: 2,
                borderColor: 'rgb(255,60,0)',
                backgroundColor:
                    'rgba(255,60,0, 0.3)',
                lineTension: 0,
                pointHoverRadius: 6,
                pointBorderColor: 'rgba(255,0,0,0.5)',
                pointBackgroundColor: 'rgb(255,255,255)',
            }]
        },
        options: {
            plugins: {
                title: {
                    display: true, // 제목 끄기
                    text: "불성실 응답 제거 후 차트",
                    font: {
                        size: 16
                    }
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

