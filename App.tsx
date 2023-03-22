import {
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  NativeModules,
  TextInput,
} from 'react-native';
import React, {useState} from 'react';

export default function App() {
  const listFont = [
    {
      id: 0,
      font: 'BlackOpsOne.ttf',
    },
    {
      id: 1,
      font: 'JosefinSans.ttf',
    },
  ];
  const SharedStorage = NativeModules.SharedStorage;
  const CallUpdateWidget = NativeModules.CallUpdateWidget;
  const [selected, setSelected] = useState<string>(listFont[0].font);

  const addWidget = () => {
    // SharedStorage.set(JSON.stringify({text: 'namlifebuoy'}));
    CallUpdateWidget.changeFont(selected);
  };

  const addWidget2 = () => {
    SharedStorage.set(JSON.stringify({text: text}));
  };

  const [text, setText] = useState('');

  return (
    <View style={styles.container}>
      {listFont.map(item => {
        return (
          <TouchableOpacity
            key={item.id}
            onPress={() => setSelected(item.font)}>
            <Text
              style={[
                styles.text,
                // eslint-disable-next-line react-native/no-inline-styles
                {borderWidth: selected === item.font ? 1 : 0},
              ]}>
              {item.font}
            </Text>
          </TouchableOpacity>
        );
      })}
      <TouchableOpacity style={styles.btn} onPress={() => addWidget()}>
        <Text style={[styles.text]}>Send</Text>
      </TouchableOpacity>
      <TextInput
        style={styles.input}
        onChangeText={newText => setText(newText)}
        value={text}
        returnKeyType="send"
        // onEndEditing={handleSubmit}
        placeholder="Enter the text to display..."
      />
      <TouchableOpacity style={styles.btn} onPress={() => addWidget2()}>
        <Text style={[styles.text]}>Send 2</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    fontSize: 20,
    color: 'blue',
  },
  btn: {
    width: 200,
    height: 50,
    marginTop: 50,
    borderRadius: 10,
    backgroundColor: 'lightblue',
    alignItems: 'center',
    justifyContent: 'center',
  },
  input: {
    width: '100%',
    borderBottomWidth: 1,
    fontSize: 20,
    minHeight: 40,
  },
});
