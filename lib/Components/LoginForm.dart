import 'package:flutter/material.dart';
import './InputField.dart';

class LoginForm extends StatefulWidget {
  LoginForm(
      {@required this.onFieldCompleted,
      this.studentIdController,
      this.pinController})
      : assert(onFieldCompleted != null);

  final FocusNode passwordFocusNode = new FocusNode();
  final ValueChanged<String> onFieldCompleted;
  final TextEditingController studentIdController, pinController;

  @override
  LoginFormState createState() => new LoginFormState();
}

class LoginFormState extends State<LoginForm> {
  final FocusNode passwordFocusNode = new FocusNode();

  bool rememberMe = true;

  @override
  void dispose() {
    passwordFocusNode.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return (new Container(
        margin: new EdgeInsets.symmetric(horizontal: 20.0),
        child: new Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              new Form(
                  child: new Column(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: <Widget>[
                    new InputField(
                      hint: "Student ID",
                      keyboardType: TextInputType.number,
                      obscure: false,
                      icon: Icons.person_outline,
                      onFieldCompleted: (value) {
                        FocusScope.of(context).requestFocus(passwordFocusNode);
                      },
                      controller: widget.studentIdController,
                    ),
                    new InputField(
                        hint: "PIN",
                        keyboardType: TextInputType.number,
                        obscure: true,
                        icon: Icons.lock_outline,
                        onFieldCompleted: widget.onFieldCompleted,
                        controller: widget.pinController,
                        focusNode: passwordFocusNode)
                  ])),
            ])));
  }
}
