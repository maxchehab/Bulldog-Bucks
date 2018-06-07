import 'package:flutter/material.dart';
import 'styles.dart';
import 'loginAnimation.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/animation.dart';
import 'dart:async';
import '../../Components/SignUpLink.dart';
import '../../Components/LoginForm.dart';
import '../../Components/SignInButton.dart';
import '../../Components/CustomTitle.dart';
import '../../Utils/WebRequest.dart';
import 'package:flutter/services.dart';
import 'package:flutter/scheduler.dart' show timeDilation;

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key key}) : super(key: key);
  @override
  LoginScreenState createState() => new LoginScreenState();
}

class LoginScreenState extends State<LoginScreen>
    with TickerProviderStateMixin {
  AnimationController _loginButtonController;
  bool playingAnimation = false;

  final TextEditingController studentIdController = new TextEditingController(),
      pinController = new TextEditingController();

  WebRequest request;

  @override
  void initState() {
    super.initState();
    _loginButtonController = new AnimationController(
        duration: new Duration(milliseconds: 3000), vsync: this);
    _loginButtonController.addStatusListener((AnimationStatus status) {
      if (status == AnimationStatus.dismissed) {
        setState(() {
          playingAnimation = false;
        });
      }
    });
  }

  @override
  void dispose() {
    _loginButtonController.dispose();
    pinController.dispose();
    studentIdController.dispose();
    super.dispose();
  }

  Future<Null> _playAnimation() async {
    try {
      await _loginButtonController.forward();
    } on TickerCanceled {}
  }

  void login() {
    setState(() {
      playingAnimation = true;
    });
    _playAnimation();
  }

  Future<bool> test() async {
    await new Future.delayed(new Duration(seconds: 2));
    return false;
  }

  Future<dynamic> validate() async {
    request ??= new WebRequest(
        studentID: studentIdController.text, pin: pinController.text);

    return await request.gatherData()
        ? ValidatingResponse.valid
        : ValidatingResponse.invalid;
  }

  @override
  Widget build(BuildContext context) {
    timeDilation = 0.4;
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);
    LoginAnimation loginAnimation = new LoginAnimation(
        validate: validate, buttonController: _loginButtonController.view);
    return (new Scaffold(
      resizeToAvoidBottomPadding: false,
      body: new Container(
          decoration: new BoxDecoration(
            image: backgroundImage,
          ),
          child: new Container(
              decoration: new BoxDecoration(
                  gradient: new LinearGradient(
                colors: <Color>[
                  const Color.fromRGBO(162, 146, 199, 0.8),
                  const Color.fromRGBO(51, 51, 63, 0.9),
                ],
                stops: [0.2, 1.0],
                begin: const FractionalOffset(0.0, 0.0),
                end: const FractionalOffset(0.0, 1.0),
              )),
              child: new ListView(
                  padding: const EdgeInsets.all(0.0),
                  children: <Widget>[
                    new Stack(
                        alignment: AlignmentDirectional.bottomCenter,
                        children: <Widget>[
                          new Column(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            children: <Widget>[
                              new CustomTitle(
                                "Bulldog Bucks",
                                height: 250.0,
                                width: 250.0,
                              ),
                              new LoginForm(
                                onFieldCompleted: (value) {
                                  login();
                                },
                                studentIdController: studentIdController,
                                pinController: pinController,
                              ),
                              new SignUpLink()
                            ],
                          ),
                          !playingAnimation
                              ? new Padding(
                                  padding: const EdgeInsets.only(bottom: 50.0),
                                  child: new InkWell(
                                      onTap: () {
                                        login();
                                      },
                                      child: new SignInButton()),
                                )
                              : loginAnimation
                        ])
                  ]))),
    ));
  }
}
