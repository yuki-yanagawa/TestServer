$(function(){
    var param = {
        test : "test12345",
    }
    $.post("postsample",{obj :JSON.stringify(param)})
    .done(function(data){
        console.log(data);
    })
    .fail(function(){
        console.log("error");
    });
});