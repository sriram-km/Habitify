const formStatus = Object.freeze({
    login: 0,
    signUp: 1,
    signUpOtp: 2,
    signUpQR: 3,
    forgotPassword: 4,
    scanQR: 5
});

var currentStatus = formStatus.login;
var phoneNumberRegex = new RegExp("^[6-9][0-9]{9}$");
var otpRegex = new RegExp("^[0-9]{6}$");

$("#button").click(function() {
    $("#phoneInput").css('box-shadow', "");
    $("#passwordInput").css('box-shadow', "");
    if (currentStatus == formStatus.login) {
        if (phoneNumberRegex.test($("#phoneInput").val()) & otpRegex.test($("#passwordInput").val())) {
            $.ajax({
                url: 'api/login/loginForm',
                type: 'POST',
                data: { 'phonenumber': $("#phoneInput").val(), 'otp': $("#passwordInput").val() },
                dataType: 'json',
                success: function(data) {
                    if (data["status"] == "100") {

                    } else if (data["status"] == "101") {
                        $("#passwordInput").css('box-shadow', "0px 0px 15px red");
                    } else if (data["status"] == "102") {
                        alert('There is no user with the number ' + $("#phoneInput").val());
                        location.reload();
                    }
                }
            });
        }
        if (!phoneNumberRegex.test($("#phoneInput").val())) {
            $("#phoneInput").css('box-shadow', "0px 0px 15px red");
        }
        if (!otpRegex.test($("#passwordInput").val())) {
            $("#passwordInput").css('box-shadow', "0px 0px 15px red");
        }
    } else if (currentStatus == formStatus.forgotPassword) {
        if (otpRegex.test($("#passwordInput").val())) {
            $("#phoneLabel").hide();
            $("#phoneInput").hide();
            $("#passwordLabel").text("Scan the QR code");
            $("#passwordInput").hide();
            $("#qr").css("display", "block");
            $("#button").text("Done");
            currentStatus = formStatus.scanQR;
        } else {
            $("#passwordInput").css('box-shadow', "0px 0px 15px red");
        }
    } else if (currentStatus == formStatus.scanQR) {

    } else if (currentStatus == formStatus.signUp) {
        if (phoneNumberRegex.test($("#phoneInput").val())) {
            $.ajax({
                url: 'api/signup/signupform',
                type: 'POST',
                data: { 'phonenumber': $("#phoneInput").val() },
                dataType: 'json',
                success: function(data) {
                    if (data["status"] == "100") {
                        $(".title").css('visibility', 'hidden');
                        $("#phoneLabel").hide();
                        $("#phoneInput").hide();
                        $("#passwordLabel").text("Enter the OTP send to your registred mobile number");
                        $("#passwordInput").text("OTP");
                        $("#passwordLabel").css("display", "block");
                        $("#passwordInput").css("display", "block");
                        $("#button").text("Next");
                        $("#forgotPassword").hide();
                        $("#signUp").hide();
                        currentStatus = formStatus.signUpOtp;
                    } else if (data["status"] == "101") {
                        alert('There was some internal error. Try after some time.');
                        location.reload();
                    } else if (data["status"] == "102") {
                        alert('A user with this phone number already exist. Try siging in');
                        location.reload();
                    }
                }
            });
        } else {
            $("#phoneInput").css('box-shadow', "0px 0px 15px red");
        }
    } else if (currentStatus == formStatus.signUpOtp) {
        if (otpRegex.test($("#passwordInput").val())) {
            $.ajax({
                url: 'api/signup/signupotp',
                type: 'POST',
                data: { 'otp': $("#passwordInput").val() },
                dataType: 'json',
                success: function(data) {
                    if (data["status"] == "100") {
                        $("#phoneLabel").hide();
                        $("#phoneInput").hide();
                        $("#passwordLabel").text("Scan the QR code");
                        $("#passwordInput").hide();
                        $("#qr").css("display", "block");
                        $("#button").text("Done");
                        currentStatus = formStatus.signUpQR;
                    } else if (data["status"] == "101") {
                        alert('Incorrect OTP. Try again');
                    } else if (data["status"] == "102") {
                        alert('There was a problem creating user. Try after some time.');
                        location.reload();
                    }
                }
            });
        } else {
            $("#passwordInput").css('box-shadow', "0px 0px 15px red");
        }
    }
});


$("#forgotPassword").click(function() {
    $("#phoneInput").css('box-shadow', "");
    $("#passwordInput").css('box-shadow', "");

    $(".title").css('visibility', 'hidden');
    $("#phoneLabel").hide();
    $("#phoneInput").hide();
    $("#passwordLabel").text("Enter the OTP send to your registred mobile number");
    $("#passwordInput").text("OTP");
    $("#button").text("Get QR code");
    $("#forgotPassword").hide();
    $("#signUp").hide();
    currentStatus = formStatus.forgotPassword;
});

$("#signUp").click(function() {
    $("#phoneInput").css('box-shadow', "");
    $("#passwordInput").css('box-shadow', "");

    $(".title").css('visibility', 'hidden');
    $("#passwordLabel").hide();
    $("#passwordInput").hide();
    $("#button").text("Sign up");
    $("#forgotPassword").hide();
    $("#signUp").hide();
    currentStatus = formStatus.signUp;
});