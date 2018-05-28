import 'package:flutter/material.dart';

typedef void OnFieldCompleted(String value);

class InputField extends StatefulWidget {
  final String hint;
  final bool obscure;
  final IconData icon;
  final OnFieldCompleted onFieldCompleted;
  final bool autofocus;
  final FocusNode focusNode;
  final TextInputType keyboardType;
  InputField(
      {this.hint,
      this.obscure,
      this.icon,
      this.onFieldCompleted,
      this.autofocus = false,
      this.focusNode,
      this.keyboardType});

  @override
  InputFieldState createState() => new InputFieldState(
      hint: hint,
      obscure: obscure,
      icon: icon,
      onFieldCompleted: onFieldCompleted,
      autofocus: autofocus,
      focusNode: focusNode,
      keyboardType: keyboardType);
}

class InputFieldState extends State<InputField> {
  final String hint;
  final bool obscure;
  final IconData icon;
  final OnFieldCompleted onFieldCompleted;
  final bool autofocus;
  final FocusNode focusNode;
  final TextInputType keyboardType;

  bool showText = false;

  InputFieldState(
      {this.hint,
      this.obscure = false,
      this.icon,
      this.onFieldCompleted,
      this.autofocus = false,
      this.focusNode,
      this.keyboardType});

  @override
  Widget build(BuildContext context) {
    return (new Container(
        decoration: new BoxDecoration(
          border: new Border(
            bottom: new BorderSide(
              width: 0.5,
              color: Colors.white24,
            ),
          ),
        ),
        child: new Stack(alignment: Alignment.bottomRight, children: [
          new TextFormField(
            keyboardType: keyboardType,
            focusNode: focusNode,
            autofocus: autofocus,
            onFieldSubmitted: (value) {
              onFieldCompleted(value);
            },
            obscureText: obscure && !showText,
            style: const TextStyle(
              color: Colors.white,
            ),
            decoration: new InputDecoration(
              icon: new Icon(
                icon,
                color: Colors.white,
              ),
              border: InputBorder.none,
              hintText: hint,
              hintStyle: const TextStyle(color: Colors.white, fontSize: 15.0),
              contentPadding: obscure
                  ? const EdgeInsets.only(
                      top: 30.0, right: 100.0, bottom: 30.0, left: 5.0)
                  : const EdgeInsets.only(
                      top: 30.0, right: 30.0, bottom: 30.0, left: 5.0),
            ),
          ),
          obscure
              ? new Padding(
                  padding: new EdgeInsets.all(30.0),
                  child: new InkWell(
                      onTap: () {
                        setState(() {
                          showText = !showText;
                        });
                      },
                      child: new Text(showText ? "Hide" : "Show",
                          style: const TextStyle(
                            color: Colors.white,
                          ))))
              : new Container(),
        ])));
  }
}
