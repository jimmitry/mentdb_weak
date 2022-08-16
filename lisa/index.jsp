<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" import="re.jpayet.mentdb.ext.server.Start,re.jpayet.mentdb.ext.bot.BotManager" %><!doctype html>
<html lang="fr" style="height:100%;">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>Bot Provider</title>
  <script src="js/jquery.min.js"></script>
  <script src="js/ECB_Blowfish.js"></script>
  <link href="dist/css/bootstrap.css" rel="stylesheet">
  <script src="dist/js/bootstrap.min.js"></script>
  <link  href="css/style.css" rel="stylesheet">
  <script src="js/Chart.bundle.js"></script>
  <script src="js/utils.js"></script>
  <link rel="icon" type="image/png" href="images/db128x128.png" />
<script>
    
var mentdb_hostname = "<% out.print(Start.WEB_SERVER_HOST); %>"; //Server hostname
var mentdb_port = "<% out.print(Start.WEB_SERVER_PORT_HTTPS); %>"; //Server port


function replaceAll(str, find, replace) {
  //return str.replace(new RegExp(find, 'g'), replace);
  return str.split(find).join(replace);
}

function init() {
    MentDBWebSocket();
}

function MentDBWebSocket() {

  try {
    websocket = new WebSocket("wss://"+mentdb_hostname+":"+mentdb_port+"/mentdb/");
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };
  }
  catch (e) {
     activeLoginMode(""+e);
  }

}

function onOpen(evt) {
    sendToServer("#login_ws \""+document.getElementById('mentdb_bot').value+"\" \""+replaceAll(document.getElementById('x-user').value, "\"", "\\\"")+"\" \""+replaceAll(document.getElementById('x-password').value, "\"", "\\\"")+"\"");
}

function onClose(evt) {
    activeLoginModeClose();
}

function sendToServer(data) {
    websocket.send(utf8_to_b64(data));
    document.getElementById('input_data').focus();
}
    
function utf8_to_b64( str ) {
    return window.btoa(unescape(encodeURIComponent( str )));
}

function b64_to_utf8( str ) {
    return decodeURIComponent(escape(window.atob( str )));
}

var nbCmd = 0;
var aiLogin = "";
function onMessage(evt) {

    nbCmd++;
    var msg = b64_to_utf8(evt.data);
    
    if (nbCmd===1) activeChatMode();
    //alert("msg="+msg);
    var obj = JSON.parse(msg);

    var value = obj["msg"];
    var login = obj["user"];
    var typeMsg = obj["type"];
    var localAi = obj["ai"];
    var strategy = '';
    
    if (obj["strategy"]) {
        strategy = obj["strategy"];
    }

    if (localAi!==null && localAi!=='') {
      aiLogin = localAi;
    }

    if (obj["bot_is_male"]) {
      if (obj["bot_is_male"]==="1") is_male = true;
      else is_male = false;
    }

    if (obj["bot_lang"]) {
      lang_mode = obj["bot_lang"];
    }
    
    if (value===null) {
      alert("null returned ...");
    } else if (value==="j12hki95orm35hrm62vni90tkmr33sdy1") {

      activeLoginMode("");

		} else if (value==="j12hki95orm35hrm62vni90tkmr33sdy4") {

      activeLoginMode("Bad user or password.");

		} else if (value==="j12hki95orm35hrm62vni90tkmr33sdy8") {

      activeLoginMode("Protocol error.");

		} else {

        
      if (nbCmd===1) showMsg(aiLogin, 1, replaceAll(replaceAll(value, '\n', '<br>'), ' ', '&nbsp;'), strategy);
      else showMsg(aiLogin, 1, value, strategy);

      window.scrollTo(0,document.body.scrollHeight);

    }

}
function onError(evt) {
  setTimeout(function () {
    activeLoginMode("Network error ...");
  }, 10);
}

function disconnectUser() {
    sendToServer("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht8");
}

function activeLoginModeClose() {
  nbCmd = 0;
  document.getElementById("output").style.display = "none";
  document.getElementById("intelBar").style.display = "none";
  document.getElementById("login").style.display = "block";
  document.getElementById("connectError").innerHTML = document.getElementById("connectError").innerHTML+"<br>Disconnected.<br>&nbsp;"

  document.getElementById("outputMsg").innerHTML = "";
}

function activeLoginMode(err) {
  nbCmd = 0;
  document.getElementById("output").style.display = "none";
  document.getElementById("intelBar").style.display = "none";
  document.getElementById("login").style.display = "block";
  if (err!="") {
    document.getElementById("connectError").innerHTML = replaceAll(err, '\n', '<br>')+"<br>&nbsp;";
  } else {
    document.getElementById("connectError").innerHTML = "";
  }
  document.getElementById("outputMsg").innerHTML = "";
}

function activeChatMode() {
  document.getElementById("output").style.display = "table";
  document.getElementById("intelBar").style.display = "block";
  document.getElementById("login").style.display = "none";
  document.getElementById("connectError").innerHTML = "";
  document.getElementById("input_data").focus();
}

function checkPassword() {

  init();

}
    
Object.defineProperty(Date.prototype, 'YYYYMMDDHHMMSS', {
    value: function() {
        function pad2(n) {  // always returns a string
            return (n < 10 ? '0' : '') + n;
        }

        return this.getFullYear() + '-'+
               pad2(this.getMonth() + 1) + '-'+
               pad2(this.getDate()) + ' '+
               pad2(this.getHours()) + ':'+
               pad2(this.getMinutes()) + ':'+
               pad2(this.getSeconds());
    }
});

function showMsg(user, type, msg, strategy) {
    
  var date = new Date();

  if (user===document.getElementById("x-user").value) {

      var html = '<div class="bubble_init_left"><img src="images/who.png" style="margin-top: 8px;float:left;width:32px;vertical-align:middle"><div class="bubble me">'+
        '  <span style="font-weight:bold;font-size:15px">'+user+'</span> &nbsp;<span style="font-size:11px;color:#aaa">'+date.YYYYMMDDHHMMSS()+'</span> &nbsp;<span style="font-size:11px;color:#E0E0E0">'+strategy+'</span>'+
        '  <div style="height:1px;background-color:#e6e6e6;margin: 5px 0px 8px 0px;"></div><p>'+msg+'</p>'+
        '</div></div>';

      $('#outputMsg').append(html);

    } else {

      var html = '<div class="bubble_init_right"><img src="images/'+(user===aiLogin?'lisa128x128':'who')+'.png" style="margin-top: 8px;float:left;width:32px;vertical-align:middle"><div class="bubble me">'+
        '  <span style="font-weight:bold;font-size:15px">'+user+'</span> &nbsp;<span style="font-size:11px;color:#aaa">'+date.YYYYMMDDHHMMSS()+'</span> &nbsp;<span style="font-size:11px;color:#E0E0E0">'+strategy+'</span>'+
        (msg==''?'':'  <div style="height:1px;background-color:#e6e6e6;margin: 5px 0px 8px 0px;"></div>'+(msg.startsWith("@")?msg.substr(1):msg))+
        '</div></div>';
        
        if (!msg.startsWith("@")) {
            speechSynt(msg);
        }
        //document.getElementById("outputMsg").innerHTML += html;
        $('#outputMsg').append(html);

    }

}

function pushData() {

  showMsg(document.getElementById("x-user").value, 1, document.getElementById('input_data').value, "");
  sendToServer(document.getElementById('input_data').value);
  document.getElementById('input_data').value = "";

}

function handle(e){

    var key = e.which || e.keyCode; // keyCode detection
    
    if ( key == 101 && e.ctrlKey ) {
        pushData();
    }

    return false;
}

</script>
</head>
<body id="body" style='background-repeat: repeat-x;
    background-attachment: fixed;
    background-position: center;
    background-size: contain;
    background-image: url(images/bg.png);height:100%;margin:0px;color:#333;font-family: monospace, Arial, Verdana, Helvetica, sans-serif;font-size: 12px;'>
<div id="login">
  <div id='divSignInCenter'>
      <div id='divSignIn' style='border: 5px #4e4e4e solid;background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #7d7d7d), color-stop(100%, #1b1b1b)), #1b1b1b;
      background: -webkit-linear-gradient(#7d7d7d, #1b1b1b), #3F3F3F;
      background: -moz-linear-gradient(#7d7d7d, #1b1b1b), #3F3F3F;
      background: -o-linear-gradient(#7d7d7d, #1b1b1b), #3F3F3F;
      background: linear-gradient(#7d7d7d, #1b1b1b), #3F3F3F;box-shadow: 2px 0px 15px 1px #888888;'>
          <div id='divSignInImg' style="padding-top: 35px;"><a href='..'><img src='images/db128x128.png' height='70px' alt=''></a></div>
          <div style='padding:5px;font-size: 16px;color:#fff;border-bottom: 1px #696969 solid;'><b>Bot Provider</b><br><span style='color:#FFF'>&nbsp;</span></div>
          <div id='connectError' style="color:#F00;min-height:25px"></div>
          <form action='index.jsp' method='post' onsubmit="checkPassword();return false;">
              <select id="mentdb_bot" name='botname' style='width: 200px;
    height: 26px;
    line-height: 26px;
    margin-bottom: 3px;'><% out.print(BotManager.get_bot_options()); %>
              </select>
              <input class='style-1' style='border: 1px #777 solid;' type='text' placeholder='Utilisateur' name='x-user' id='x-user' autofocus><br>
              <input class='style-1' style='border: 1px #777 solid;' type='password' placeholder='Mot de passe' name='x-password' id='x-password'>
              <input type="submit" class='btn btn-dark' style='font-size:12px;padding: 5px 30px 5px 30px;margin-top: 10px;width: 200px;height: 30px;' value="Connexion">
              <div style='height:25px'></div>
          </form>
      </div>
      <div style='margin-top: 10px;color:#fff'>&copy; 2012 - 2019 - <a href='https://www.mentdb.org' style='color:#fff'><b>MentDB</b></a></div>
  </div>
</div>
<table id='output' style='display:none;height:100%;width:100%;'><tr><td style="vertical-align: bottom;background-repeat: repeat-x;
    background-attachment: fixed;
    background-position: center;
    background-size: contain;
    background-image: url(images/bg.png);">
  <div id='outputMsg' class="chat">

  </div>
  <div style="height: 68px;width: 100%;display: inline-block;"></div>
</td></tr></table>
<div id="intelBar" style="display:none;font-size:13px;margin:0px;font-family: 'Helvetica Neue', Roboto, Arial, 'Droid Sans', sans-serif;">
  <div style='bottom: 0px;position: fixed;width: 100%;right:0px'>
    <div style='width: 100%;-webkit-border-radius: 34px;-moz-border-radius: 34px;border-radius: 34px;float: right;background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b6479), color-stop(100%, #4c5566)), #686e78;
      background: -webkit-linear-gradient(#444, #333), #3F3F3F;
      background: -moz-linear-gradient(#444, #333), #3F3F3F;
      background: -o-linear-gradient(#444, #333), #3F3F3F;
      background: linear-gradient(#444, #333), #3F3F3F;background-color: #F0F0F0;height:67px;color:#E7E7E7;line-height:65px;'>
      <a href='javascript:pushData()' style='float: left;'><img src="images/db128x128.png" style='width: 65px;vertical-align: middle;margin-left: 1px;'></a>
      <textarea id="input_data" style="font-size: 15px;
        border: 0px;
        border-left: 1px #777 solid;
        border-right: 1px #777 solid;
        vertical-align: middle;
        display: inline-block;
        box-sizing: border-box;
        height: 54px;
        resize: none;
        padding: 10px 15px 10px 15px;
        margin: 5px 0px 6px 2px;
        box-sizing: border-box;
        width: calc(100% - 170px);
        outline: none;
        line-height: 20px;
        color: #E7E7E7;
        background: -webkit-gradient(linear, 50% 0%, 50% 100%, color-stop(0%, #5b6479), color-stop(100%, #4c5566)), #686e78;
        background: -webkit-linear-gradient(#444, #333), #3F3F3F;
        background: -moz-linear-gradient(#444, #333), #3F3F3F;
        background: -o-linear-gradient(#444, #333), #3F3F3F;
        background: linear-gradient(#444, #333), #3F3F3F;
        background-color: #F0F0F0;"></textarea>
        <img id='speechReco' src='images/speech_stop.png' style='width: 40px;margin-left:-50px;cursor: pointer' onclick="startDictation();">
      <a style='text-decoration:none;padding: 0px 20px 0px 10px;color: #fff;float: right;margin-right: 10px;font-size:15px' href='javascript:disconnectUser()'>Logout</a>
    </div>
  </div>
</div>
<script>

        window.onload = function() {

            $("#input_data").keydown(function(e) {
                if (e.keyCode == 69 && (e.metaKey || e.ctrlKey)) {
                    pushData();
                }
            });

        };

        activeLoginMode("");

    var dictation_started = false;
    var lang_mode = "fr";
    var is_male = false;
    var json_dictation = {
        'fr': "fr-FR",
        'en': "en-GB"
    };
    var json_voice = {
        'fr': "Amelie",
        'en': "Samantha"
    };
    var json_voice_male = {
        'fr': "Thomas",
        'en': "Daniel"
    };
    
    var send_voice_auto = true;

    //SpeechRecognition
    function startDictation() {

        if (!dictation_started) {

            if (window.hasOwnProperty('webkitSpeechRecognition')) {

              dictation_started = true;
              var recognition = new webkitSpeechRecognition();

              recognition.continuous = false;
              recognition.interimResults = false;

              recognition.lang = json_dictation[lang_mode];
              recognition.start();
              document.getElementById('speechReco').src="images/speech_start.png";

              recognition.onresult = function(e) {
                var text = e.results[0][0].transcript
                var first_upper = text.charAt(0).toUpperCase() + text.substr(1);
                document.getElementById('input_data').value = document.getElementById('input_data').value + first_upper;
                recognition.stop();
                document.getElementById('speechReco').src="images/speech_stop.png";
                if (send_voice_auto) pushData();
                dictation_started = false;
              };

              recognition.onerror = function(e) {
                document.getElementById('speechReco').src="images/speech_stop.png";
                recognition.stop();
                dictation_started = false;
                alert('Speech recognition error detected: ' + e.error);
              }
            } else {

                alert("Sorry, this feature work only with 'Google Chrome'.");

            }

        }

    }

    var synth = window.speechSynthesis;
    var voices = [];
    function populateVoiceList() {

        voices = synth.getVoices();

    }

    populateVoiceList();
    if (speechSynthesis.onvoiceschanged !== undefined) {
      speechSynthesis.onvoiceschanged = populateVoiceList;
    }

    //SpeechRecognition
    function speechSynt(inputTxt) {
        
        if (window.hasOwnProperty('webkitSpeechRecognition')) {

            var utterThis = new SpeechSynthesisUtterance(replaceAll(inputTxt, "<br>", ""));
            if (is_male) {
                for(i = 0; i < voices.length ; i++) {
                    if(voices[i].name === json_voice_male[lang_mode]) {
                      utterThis.voice = voices[i];
                    }
                }
            } else {
                for(i = 0; i < voices.length ; i++) {
                    if(voices[i].name === json_voice[lang_mode]) {
                      utterThis.voice = voices[i];
                    }
                }
            }

            utterThis.pitch = 0.6;
            utterThis.rate = 1.0;
            synth.speak(utterThis);

        }

    }
        
</script>
</body>
</html>
