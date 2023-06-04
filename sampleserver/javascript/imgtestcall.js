$(function(){
    $("#bt").on('click',function(e){
        $.post("/imgRequest",JSON.stringify({imgNo : "test.png"}))
        .done(function(data){
            //console.log(data);
            $("#imgtest")[0].src = "data:image/png;base64," + data.data;
        })
        .fail(function(){

        })
    });
});