`use strict`;
//массив со всеми тегами на отправку
let allTagsArr=[];
let tagInput = document.querySelector(`#tags`);
tagInput.addEventListener(`input`, ()=>{
    if (tagInput.value[tagInput.value.length-1]===`,`){
        let newTag = tagInput.value.slice(0,-1);
        allTagsArr.push(newTag);
        let newTagDiv = document.createElement(`div`);
        newTagDiv.classList.add(`tag`);
        newTagDiv.textContent = newTag;
        tagInput.parentElement.appendChild(newTagDiv);
        tagInput.value= ``;
        console.log(allTagsArr);
    }
})
