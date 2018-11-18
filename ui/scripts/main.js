let losts = [ ];

let lostActiveIndex = 0;
let matchesActiveIndex = 0;
let matches = [];

let loadLosts = () => {
    let success = (returnLosts) => {
        if (returnLosts && returnLosts.length) {
            losts = returnLosts;
            matches = losts[lostActiveIndex].foundImages;
            initLostsComponent();
            initMatchesComponent();
        } else {
            console.error("Something wrong?!")
        }
    };
     $.get("/matches", success);
};

let initMatchesComponent = () => {
    let matchesInnerContainer = $("#matches .carousel-inner");
    matchesInnerContainer.empty();
    matches.forEach((match, index) => {
        let { foundImagePath } = match;
        let active = index === 0 ? 'active' : '';
        let item = `<div class='carousel-item ${active}'><img class='d-block' src='${foundImagePath}'></div>`;
       matchesInnerContainer.append(item);
    });
    $('#matches').carousel();
    updateMissingInfo(matches[matchesActiveIndex]);
}

let initLostsComponent = () => {
    losts.forEach((lost, index) => {
        let { lostImagePath } = lost;
        let active = index === 0 ? 'active' : '';
        let item = `<div class='carousel-item ${active}'><img class='d-block' src='${lostImagePath}'></div>`;
        $("#losts .carousel-inner").append(item);
    });
    $('#losts').carousel();
}

$("#losts .carousel-control-prev").on("click", (e) => {
    if (!losts || !losts.length) return;
    if (lostActiveIndex === 0) {
        lostActiveIndex = losts.length - 1;
    } else {
        lostActiveIndex--;
    }
    updateMatches(losts[lostActiveIndex].foundImages);
});

$("#losts .carousel-control-next").on("click", (e) => {
    if (!losts || !losts.length) return;
    if (lostActiveIndex === losts.length - 1) {
        lostActiveIndex = 0;
    } else {
        lostActiveIndex++;
    }
    updateMatches(losts[lostActiveIndex].foundImages);
});

let updateMatches = (newMatches) => {
    matches = newMatches;
    matchesActiveIndex = 0;
    initMatchesComponent();
};

let progBar = $(".progress-bar.field-value");
let updateMissingInfo = (info) => {
    let { matchPercentage: similarity } = info;
    $(".match-info .similarity").text(`${similarity}%`);
    
    progBar.removeClass(["bg-success", "bg-warning", "bg-info"]);
    if(+similarity >= 85) {
        progBar.addClass("bg-success");
    } else if (+similarity < 50) {
        progBar.addClass("bg-warning");   
    } else {
        progBar.addClass("bg-info");
    }
    progBar.css("width", `${similarity}%`);
};

$("#matches .carousel-control-prev").on("click", (e) => {
    if (!matches || !matches.length) return;
    if (matchesActiveIndex === 0) {
        matchesActiveIndex = matches.length - 1;
    } else {
        matchesActiveIndex--;
    }
    let missingInfo = matches[matchesActiveIndex];
    setTimeout(() => updateMissingInfo(missingInfo), 100);
});

$("#matches .carousel-control-next").on("click", (e) => {
    if (!matches || !matches.length) return;
    if (matchesActiveIndex === matches.length - 1) {
        matchesActiveIndex = 0;
    } else {
        matchesActiveIndex++;
    }
    let missingInfo = matches[matchesActiveIndex];
    setTimeout(() => updateMissingInfo(missingInfo), 100);
});

$("#confirmMatched").on("click", (e) => {
    console.log("Matched:", JSON.stringify(matches[matchesActiveIndex]));
})

$("#closeThanksModal").on("click", (e) => {
    setTimeout(() => {
        window.location.href = "home.html";
    }, 500);
});

$(() => {
    loadLosts();
});