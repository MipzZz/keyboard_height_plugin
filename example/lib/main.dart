import 'package:flutter/material.dart';
import 'package:keyboard_height_plugin/keyboard_height_plugin.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple)),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  void initState() {
    super.initState();
    _keyboardHeightPlugin.onKeyboardHeightChanged((double height) {
      setState(() {
        _keyboardHeight = height;
      });
    });
  }

  double _keyboardHeight = 0;
  final _keyboardHeightPlugin = KeyboardHeightPlugin();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.end,
          children: <Widget>[
            Text('Keyboard height: $_keyboardHeight'),
            TextField(
              onTapUpOutside: (_) => FocusManager.instance.primaryFocus?.unfocus(),
            ),
            SizedBox(height: _keyboardHeight),
          ],
        ),
      ),
    );
  }
}
