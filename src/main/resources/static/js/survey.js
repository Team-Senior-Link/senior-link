let currentTab = 0;
function showTab(index) {
    const tabs = document.querySelectorAll('#tabContent > div');
    const tabMenuItems = document.querySelectorAll('#tabMenu li');
    tabs.forEach((tab, i) => {
        tab.classList.toggle('hidden', i !== index);
        tabMenuItems[i].classList.toggle('active', i === index);
    });

    currentTab = index;

    if(currentTab === 99){
        document.querySelector('.result_box').classList.toggle('hidden')
        document.querySelector('.result_box').classList.toggle('active')
        document.querySelector('.btnWrap .gray').style.display = 'none';
        document.querySelector('.btnWrap .pink').style.display = 'none';
        document.querySelector('#submitBtn').style.display = 'none';
    }else {

        document.querySelector('.btnWrap .gray').style.display = index === 0 ? 'none' : 'inline-block';
        document.querySelector('.btnWrap .pink').style.display = index === tabs.length - 1 ? 'none' : 'inline-block';
        document.querySelector('#submitBtn').style.display = index === tabs.length - 1 ? 'inline-block' : 'none';
    }

}

function nextTab() {
    if (validateTab(currentTab)) {
        showTab(currentTab + 1);
    } else {
        alert("모든 질문에 응답해 주세요.");
    }
}

function showResult(){
    let score = 0;
    let level = '6';
    const checked = document.querySelectorAll('input[type="radio"]:checked');
    checked.forEach(d=>score += Number(d.value))

    if(score >= 95){
        level = '1';
    }else if(score >= 75){
        level = '2';
    }else if(score >= 60){
        level = '3';
    }else if(score >= 51){
        level = '4';
    }else if(score >= 45){
        level = '5';
    }
    document.querySelector('#rs_score').textContent = score + '점';
    document.querySelector('#rs_level').textContent = level == 6? '인지지원등급('+ level + '등급)': level +'등급';
    document.querySelector('#level'+level).classList.toggle('hidden')
    showTab(99);
}

function prevTab() {
    showTab(currentTab - 1);
}

function submitForm() {
     const tabs = document.querySelectorAll('#tabContent > div');
    for (let i = 0; i < tabs.length; i++) {
        if (!validateTab(i)) {
            alert("모든 질문에 응답해 주세요.");
            return;
        }
    }
    // 백엔드 통신 트리거
    //document.getElementById("surveyForm").submit();
    showResult()
}

function validateTab(index) {
    const currentTabInputs = document.querySelectorAll(`#tabContent > div:nth-child(${index + 1}) input`);
    return Array.from(currentTabInputs).some(input => input.checked);
}

document.addEventListener("DOMContentLoaded", function() {
    showTab(0);
});