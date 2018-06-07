import 'package:html/parser.dart' show parse;
import 'package:html/dom.dart';
import 'dart:async';
import 'dart:io';
import 'package:dio/dio.dart';
import 'package:intl/intl.dart';
import 'UserData.dart';
import 'Transaction.dart';

class WebRequest {
  WebRequest({String studentID, String pin})
      : credentials = {"PIN": pin, "sid": studentID};
  final Map<String, String> credentials;
  final String _loginURL =
      "https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_ValLogin";

  final String _logoutURL =
      "https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_Logout";

  final String _transactionURL =
      "https://zagweb.gonzaga.edu/pls/gonz/hwgwcard.transactions";

  Dio _dio = new Dio();

  static UserData userData;

  Future<bool> gatherData() async {
    try {
      await _dio.post(_loginURL,
          data: new FormData.from(credentials),
          options: new Options(headers: {"Cookie": "TESTID=set;"}));
    } on DioError catch (e) {
      print(e.response.data);
      print(e.response.headers);
      print(e.response.request);
      return false;
    }

    List<Cookie> cookies = _dio.cookieJar.loadForRequest(new Uri(
        host: "zagweb.gonzaga.edu",
        port: 443,
        path: "/pls/gonz/twbkwbis.P_ValLogin"));

    if (!_cookiesContain("SESSID", cookies)) {
      return false;
    }

    bool parsedDataSuccess = true;
    try {
      Response response = await _dio.get(_transactionURL,
          options: new Options(
              headers: {"Cookie": "SESSID=" + _getSessid(cookies) + ";"}));
      userData = _parseData(response.data);
    } on DioError catch (e) {
      print(e.response.data);
      print(e.response.headers);
      print(e.response.request);
      return false;
    } catch (e) {
      print(e.toString());
      parsedDataSuccess = false;
    }

    cookies = _dio.cookieJar.loadForRequest(new Uri(
        host: "zagweb.gonzaga.edu",
        port: 443,
        path: "/pls/gonz/hwgwcard.transactions"));

    if (!_cookiesContain("SESSID", cookies)) {
      return false;
    }

    try {
      await _dio.get(_logoutURL,
          options: new Options(
              headers: {"Cookie": "SESSID=" + _getSessid(cookies) + ";"}));
    } on DioError catch (e) {
      print(e.response.data);
      print(e.response.headers);
      print(e.response.request);
      return false;
    }
    return parsedDataSuccess;
  }

  UserData _parseData(String body) {
    Document document = parse(body);
    RegExp balanceExp = new RegExp(r".*?(\$[0-9]+(?:\.[0-9][0-9])?)(?![\d])",
        caseSensitive: false);
    RegExp numbersExp = new RegExp(r"[^0-9.]");
    double balance = double.tryParse(
        balanceExp.firstMatch(body).group(1).replaceAll(numbersExp, ''));
    bool frozen = body.contains("Unfreeze my card now");
    List<Element> tables = document.getElementsByClassName("plaintable");

    List<Transaction> transactions = new List<Transaction>();
    for (Element table in tables) {
      if (table.outerHtml.contains("Transaction Date")) {
        List<Element> rows = table.getElementsByTagName("tr");
        rows.removeAt(0);
        rows.removeAt(rows.length - 1);
        for (Element row in rows) {
          List<Element> properties = row.getElementsByClassName("pllabel");
          String dateValue = properties[0]
              .innerHtml
              .replaceFirst(' pm', ' PM')
              .replaceFirst(' am', ' AM');
          DateFormat format = new DateFormat("MM/dd/yyyy hh:mm:ss a");
          transactions.add(new Transaction(
              time: format.parse(dateValue),
              location: properties[1].innerHtml,
              accountType: properties[2].innerHtml,
              amount: double
                  .parse(properties[3].innerHtml.replaceAll(numbersExp, '')),
              status: properties[4].innerHtml,
              deniedMessage: properties[5].innerHtml,
              type: properties[6].innerHtml.contains("Sale")
                  ? TransactionType.Sale
                  : TransactionType.Deposit));
        }
        break;
      }
    }

    return new UserData(
        transactions: transactions, balance: balance, frozen: frozen);
  }

  bool _cookiesContain(String name, List<Cookie> cookies) {
    for (Cookie cookie in cookies) {
      if (cookie.name == "SESSID") {
        return true;
      }
    }

    return false;
  }

  String _getSessid(List<Cookie> cookies) {
    for (Cookie cookie in cookies) {
      if (cookie.name == "SESSID") {
        return cookie.value;
      }
    }
    return "";
  }
}
