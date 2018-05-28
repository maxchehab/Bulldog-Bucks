import 'package:flutter/material.dart';
import './InputField.dart';
import 'package:flutter/scheduler.dart' show timeDilation;

class LoginForm extends StatefulWidget {
  LoginForm({this.onFieldCompleted});
  final OnFieldCompleted onFieldCompleted;

  @override
  LoginFormState createState() =>
      new LoginFormState(onFieldCompleted: onFieldCompleted);
}

class LoginFormState extends State<LoginForm> {
  LoginFormState({this.onFieldCompleted});

  final FocusNode passwordFocusNode = new FocusNode();
  final OnFieldCompleted onFieldCompleted;

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
                hint: "User ID",
                keyboardType: TextInputType.number,
                obscure: false,
                icon: Icons.person_outline,
                onFieldCompleted: (value) {
                  FocusScope.of(context).requestFocus(passwordFocusNode);
                },
              ),
              new InputField(
                  hint: "PIN",
                  keyboardType: TextInputType.number,
                  obscure: true,
                  icon: Icons.lock_outline,
                  onFieldCompleted: onFieldCompleted,
                  focusNode: passwordFocusNode)
            ],
          )),
        ],
      ),
    ));
  }
}
