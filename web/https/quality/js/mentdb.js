function execute_script(scriptname, OBJ_OVERWRITE_B64, vals_keys, client_data) {
    
    if (vals_keys !== undefined && vals_keys!=='') {
        
        var OBJ_OVERWRITE = JSON.parse(atob(OBJ_OVERWRITE_B64));
        var val_key = vals_keys.split(',');
        for (var i = 0; i < val_key.length; i++) {
            var v_k = val_key[i];
            var vk = v_k.split('=');
            
            OBJ_OVERWRITE[vk[0]] = document.getElementById(vk[1]).value;
        }
        
        OBJ_OVERWRITE_B64 = base64EncodeUnicode(JSON.stringify(OBJ_OVERWRITE));
        
    }
    
    if (client_data !== undefined && client_data!=='') {
        
        if (atob(client_data).indexOf("@@@")==0) {
            
            eval(atob(client_data).substring(3));
            
        } else {
        
            var OBJ_OVERWRITE = JSON.parse(atob(OBJ_OVERWRITE_B64));
            //alert(""+atob(client_data));
            OBJ_OVERWRITE["client_data"] = eval(atob(client_data));
            //alert("jsonA="+JSON.stringify(OBJ_OVERWRITE));
            OBJ_OVERWRITE_B64 = base64EncodeUnicode(JSON.stringify(OBJ_OVERWRITE));
            //alert("jsonB="+OBJ_OVERWRITE_B64);
            //console.log("jsonB="+OBJ_OVERWRITE_B64);
        
            execute_b64(encode_script(atob(scriptname), OBJ_OVERWRITE_B64));
        
        }  
        
    } else {
    
        execute_b64(encode_script(atob(scriptname), OBJ_OVERWRITE_B64));
        
    }
    
}

function base64EncodeUnicode(str) {
    // First we escape the string using encodeURIComponent to get the UTF-8 encoding of the characters, 
    // then we convert the percent encodings into raw bytes, and finally feed it to btoa() function.
    utf8Bytes = encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
            return String.fromCharCode('0x' + p1);
    });

    return btoa(utf8Bytes);
}

function encode_script(scriptname, OBJ_OVERWRITE_B64) {
    
    return base64EncodeUnicode("include \""+scriptname+"\" \"[OBJ_OVERWRITE_B64]\" \""+OBJ_OVERWRITE_B64+"\";");
    
}

function check_form_values(form_id) {
    
    var o = {};
    var form = $("#"+form_id);
    var a = form.serializeArray();
    var b = true;
    $.each(a, function() {
        var f = document.forms[form_id][this.name];
        if((this.value || '')==='' && f.hasAttribute('required')) {
            if (f.style) {
                f.style.border = "4px #F00 solid";
                b = false;
            }
        } else {
            if (f.style) {
                f.style.border = "0px";
            }
        }
    });
    
    return b;
    
}

var current_top_icon = "";
    
function execute_b64(query) {
    //alert(""+query);
    
    if (current_top_icon==="") {
        current_top_icon = document.getElementById("top_refresh_icon").src;
    }
        
    document.getElementById("top_refresh_icon").src="images/refresh.gif";
    
    $.ajax({
      type: 'POST',
      url: "index.jsp",
      data: {mentdb_ajax_call: "1", mql: query},
      dataType: "text",
      async:true,
      cache:false,
      success: function(result) {
          
            try {
                
                var result_list = JSON.parse(result);

                for (var i = 0; i < result_list.length; i++) {
                    var obj = result_list[i];

                    if (obj.type.substring(0, 6)==='ALERT_') {

                        document.getElementById("mentdb_alert_container").innerHTML="<div class=\"alert alert-"+obj.type.substring(6).toLowerCase()+" alert-dismissible fade show\" role=\"alert\" style='position: fixed;top: 30px;left:25%;width: 50%;z-index: 99999;' data-dismiss=\"alert\">"+
                            "  <strong>"+obj.strong.replace(/\n/g, '<br>')+"</strong> &nbsp; "+obj.msg.replace(/\n/g, '<br>')+
                            "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">"+
                            "    <span aria-hidden=\"true\">&times;</span>"+
                            "  </button>"+
                            "</div>";

                        window.setTimeout(function() {
                            $(".alert").fadeTo(500, 0).slideUp(500, function(){
                                $(this).remove(); 
                            });
                        }, 5000);

                    } else if (obj.type==='REFRESH') {
                        
                        if (obj.target === '') obj.target = "mentdb_modal_container"; 
                        document.getElementById(obj.target).innerHTML = "";
                        $('#'+obj.target).append(obj.innerHtml);

                    } else if (obj.type==='JAVASCRIPT') {

                        eval(obj.javascript);

                    }

                }
                
            } catch(error) {
                
                document.getElementById("mentdb_alert_container").innerHTML="<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" style='position: fixed;top: 30px;left:25%;width: 50%;z-index: 99999;' data-dismiss=\"alert\">"+
                    "  <strong>KO !</strong> &nbsp; "+result.replace(/\n/g, '<br>')+
                    "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">"+
                    "    <span aria-hidden=\"true\">&times;</span>"+
                    "  </button>"+
                    "</div>";

                window.setTimeout(function() {
                    $(".alert").fadeTo(500, 0).slideUp(500, function(){
                        $(this).remove(); 
                    });
                }, 5000);
                
            }
          
            document.getElementById("top_refresh_icon").src = current_top_icon;

        },
      error: function (textStatus, errorThrown) {
          
            document.getElementById("mentdb_alert_container").innerHTML="<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\" style='position: fixed;top: 30px;left:25%;width: 50%;z-index: 99999;' data-dismiss=\"alert\">"+
                "  <strong>KO !</strong> &nbsp; "+textStatus.replace(/\n/g, '<br>')+": "+errorThrown.replace(/\n/g, '<br>')+
                "  <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">"+
                "    <span aria-hidden=\"true\">&times;</span>"+
                "  </button>"+
                "</div>";
          
            window.setTimeout(function() {
                $(".alert").fadeTo(500, 0).slideUp(500, function(){
                    $(this).remove(); 
                });
            }, 5000);
          
            document.getElementById("top_refresh_icon").src = current_top_icon;
          
        }
    });
    
}

$.fn.serializeObject_file_upload = function(scriptname, OBJ_OVERWRITE_B64) {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    
    var form = $(this);
    var nb_file = 0;
    var tab_file = [];
    var send = false;
    
    $.each(form.find('input[type="file"]'), function(i, tag) {
      $.each($(tag)[0].files, function(i, file) {
          
            nb_file++;
          
      });
    });
    
    if (nb_file==0) {
        
        var OBJ_OVERWRITE = JSON.parse(atob(OBJ_OVERWRITE_B64));
        //alert(""+atob(client_data));

        OBJ_OVERWRITE["client_data"] = o;
        //alert("jsonA="+JSON.stringify(OBJ_OVERWRITE));
        OBJ_OVERWRITE_B64 = base64EncodeUnicode(JSON.stringify(OBJ_OVERWRITE));
        //alert("jsonB="+OBJ_OVERWRITE_B64);
        //console.log("jsonB="+OBJ_OVERWRITE_B64);

        execute_b64(encode_script(atob(scriptname), OBJ_OVERWRITE_B64));
        
    } else {
    
        $.each(form.find('input[type="file"]'), function(i, tag) {
          $.each($(tag)[0].files, function(i, file) {

                var reader = new FileReader();
                reader.onload = function() {
                    o[tag.name+"_datafile"] = reader.result.substring(reader.result.indexOf(",")+1);
                    o[tag.name+"_filename"] = document.getElementById(tag.name).value;
                    
                    //console.log(tag.name+"A3:"+Date.now());
                    tab_file.push(tag.name);
                    if (tab_file.length===nb_file) {

                        if (!send) {
                            send = true;

                            var OBJ_OVERWRITE = JSON.parse(atob(OBJ_OVERWRITE_B64));
                            //alert(""+atob(client_data));

                            OBJ_OVERWRITE["client_data"] = o;
                            //alert("jsonA="+JSON.stringify(OBJ_OVERWRITE));
                            OBJ_OVERWRITE_B64 = base64EncodeUnicode(JSON.stringify(OBJ_OVERWRITE));
                            //alert("jsonB="+OBJ_OVERWRITE_B64);
                            //console.log("jsonB="+OBJ_OVERWRITE_B64);

                            execute_b64(encode_script(atob(scriptname), OBJ_OVERWRITE_B64));
                        }

                    }
                };
                //console.log("A1:"+Date.now());
                reader.readAsDataURL(file); 
                //console.log("A2:"+Date.now());

          });
        });
        
    };
    
};

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    
    return o;
};

String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

function check_all(control_name, main_checkbox)
{
    var cases = document.getElementsByName(control_name);
    var bool = false;
    if(main_checkbox.checked) {
        bool = true;
    }
    for(var i=0; i<cases.length; i++) {
        cases[i].checked = bool;
    }
}
function get_all_checked(control_name) {
    
    var result = [];
    var cases = document.getElementsByName(control_name);
    
    for(var i=0; i<cases.length; i++) {
        if (cases[i].checked) {
            var obj = JSON.parse(atob(cases[i].value));
            result.push(obj);
        }
    }
    
    return result;
    
}