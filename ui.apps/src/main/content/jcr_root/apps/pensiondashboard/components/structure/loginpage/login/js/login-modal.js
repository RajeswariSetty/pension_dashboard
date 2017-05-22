$("#loginModalForm").submit(function(e) {
    var form = $("#loginModalForm"),
        username = $("#loginModalForm [name='j_username']").val(),
        password = $("#loginModalForm  [name='j_password']").val(),
        resource = $("#loginModalForm  [name='resource']").val();
    $("#loginModal .alert").hide();
    if (username && password) {
        $.post(form.attr("action"), {
            'j_username' : username,
            'j_password' : password,
            'j_validate' : "true",
            'resource'   : resource
        }, function() {
            CQ_Analytics.ProfileDataMgr.clear();
            CQ_Analytics.ProfileDataMgr.loadProfile(username);
            window.location.href=resource;
        }).fail(function(jqXHR, textStatus) {
            var result = $.trim(jqXHR.responseText);
            if (result === "invalid_login") {
                $("#loginModalInvalid").show();
            } else {
                $("#loginModalUnknown").show();
            }
        });
    } else {
        $("#loginModalRequired").show();
    }
    e.preventDefault();
});