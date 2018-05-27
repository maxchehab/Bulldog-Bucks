import 'package:flutter/material.dart';
import 'styles.dart';
import 'loginAnimation.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/animation.dart';
import 'dart:async';
import '../../Components/SignUpLink.dart';
import '../../Components/LoginForm.dart';
import '../../Components/SignInButton.dart';
import '../../Components/BulldogTitle.dart';
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
  var animationStatus = 0;

  @override
  void initState() {
    super.initState();
    _loginButtonController = new AnimationController(
        duration: new Duration(milliseconds: 3000), vsync: this);
  }

  @override
  void dispose() {
    _loginButtonController.dispose();
    super.dispose();
  }

  Future<Null> _playAnimation() async {
    try {
      await _loginButtonController.forward();
      await _loginButtonController.reverse();
    } on TickerCanceled {}
  }

  @override
  Widget build(BuildContext context) {
    timeDilation = 0.4;
    SystemChrome.setSystemUIOverlayStyle(SystemUiOverlayStyle.dark);

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
                          new BulldogTitle(),
                          new LoginForm(),
                          new SignUpLink()
                        ],
                      ),
                      animationStatus == 0
                          ? new Padding(
                              padding: const EdgeInsets.only(bottom: 50.0),
                              child: new InkWell(
                                  onTap: () {
                                    setState(() {
                                      animationStatus = 1;
                                    });
                                    _playAnimation();
                                  },
                                  child: new SignInButton()),
                            )
                          : new StaggerAnimation(
                              buttonController: _loginButtonController.view),
                    ],
                  ),
                ],
              ))),
    ));
  }
}
