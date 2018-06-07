import 'package:flutter/material.dart';
import 'dart:async';

enum ValidatingResponse { valid, invalid, validating }

typedef Future<dynamic> LoginValidationCallback();

class LoginAnimation extends StatelessWidget {
  LoginAnimation(
      {Key key, @required this.validate, @required this.buttonController})
      : assert(validate != null),
        assert(buttonController != null),
        buttonSqueezeAnimation = new Tween(
          begin: 320.0,
          end: 70.0,
        ).animate(
          new CurvedAnimation(
            parent: buttonController,
            curve: new Interval(
              0.0,
              0.150,
            ),
          ),
        ),
        buttomZoomOut = new Tween(
          begin: 70.0,
          end: 1000.0,
        ).animate(
          new CurvedAnimation(
            parent: buttonController,
            curve: new Interval(
              0.550,
              0.999,
              curve: Curves.bounceOut,
            ),
          ),
        ),
        containerCircleAnimation = new EdgeInsetsTween(
          begin: const EdgeInsets.only(bottom: 50.0),
          end: const EdgeInsets.only(bottom: 0.0),
        ).animate(
          new CurvedAnimation(
            parent: buttonController,
            curve: new Interval(
              0.500,
              0.800,
              curve: Curves.ease,
            ),
          ),
        ),
        super(key: key);

  final AnimationController buttonController;
  final Animation<EdgeInsets> containerCircleAnimation;
  final Animation buttonSqueezeAnimation;
  final Animation buttomZoomOut;
  final LoginValidationCallback validate;
  bool asked = false;

  Widget _buildAnimation(BuildContext context, Widget child) {
    return new Padding(
        padding: buttomZoomOut.value == 70
            ? const EdgeInsets.only(bottom: 50.0)
            : containerCircleAnimation.value,
        child: new Hero(
            tag: "fade",
            child: buttomZoomOut.value <= 300
                ? new Container(
                    width: buttomZoomOut.value == 70
                        ? buttonSqueezeAnimation.value
                        : buttomZoomOut.value,
                    height:
                        buttomZoomOut.value == 70 ? 60.0 : buttomZoomOut.value,
                    alignment: FractionalOffset.center,
                    decoration: new BoxDecoration(
                      color: Theme.of(context).accentColor,
                      borderRadius: buttomZoomOut.value < 400
                          ? new BorderRadius.all(const Radius.circular(30.0))
                          : new BorderRadius.all(const Radius.circular(0.0)),
                    ),
                    child: buttonSqueezeAnimation.value > 75.0
                        ? new Text(
                            "Sign In",
                            style: new TextStyle(
                              color: Colors.white,
                              fontSize: 20.0,
                              fontWeight: FontWeight.w300,
                              letterSpacing: 0.3,
                            ),
                          )
                        : buttomZoomOut.value < 300.0
                            ? new CircularProgressIndicator(
                                value: null,
                                strokeWidth: 1.0,
                                valueColor: new AlwaysStoppedAnimation<Color>(
                                    Colors.white),
                              )
                            : null)
                : new Container(
                    width: buttomZoomOut.value,
                    height: buttomZoomOut.value,
                    decoration: new BoxDecoration(
                      shape: buttomZoomOut.value < 500
                          ? BoxShape.circle
                          : BoxShape.rectangle,
                      color: Theme.of(context).accentColor,
                    ))));
  }

  buttonSqueezeListener() async {
    if (buttonSqueezeAnimation.value == 70 && !asked) {
      asked = true;
      buttonController.stop();
      ValidatingResponse response = await validate();
      if (response == ValidatingResponse.valid) {
        buttonController.forward();
      } else if (response == ValidatingResponse.invalid) {
        buttonController.reverse();
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    buttonSqueezeAnimation.addListener(buttonSqueezeListener);

    buttonController.addStatusListener((AnimationStatus status) {
      if (status == AnimationStatus.completed ||
          status == AnimationStatus.dismissed) {
        buttonSqueezeAnimation.removeListener(buttonSqueezeListener);
      }
    });

    buttonController.addListener(() async {
      if (buttonController.isCompleted) {
        Navigator
            .of(context)
            .pushNamedAndRemoveUntil('/home', (Route<dynamic> route) => false);
      }
    });
    return new AnimatedBuilder(
      builder: _buildAnimation,
      animation: buttonController,
    );
  }
}
