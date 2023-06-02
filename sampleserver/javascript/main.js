$(function(){
    $("#bt").on('click',function(e){
        postTest();
    });
})

function sleep(sleeptime) {
    var startdate = new Date();
    while (new Date() - startdate < sleeptime);
    console.log("sleep End .....")
}

function testconsole() {
    sleep(5000);
    console.log("call stack Check!!");
}

function postTest() {
    // 非同期通信を同期的にコントロール
    var def = $.Deferred();
    $.post("test.json")
    .done(function(data){
        console.log("OK1!!");
        // 非同期通信を同期的にコントロール
        def.resolve();
    })
    .fail(function(){
        // 非同期通信を同期的にコントロール
        def.reject();
    })
    // 非同期通信を同期的にコントロール
    return def.promise();
    //sleep(5000)
}

debugger
// 同期はしない
// postTest();
// testconsole();

// 非同期通信を同期的にコントロール
postTest()
.then(function(){
    testconsole()
});