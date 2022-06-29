import logo from './logo.svg';
import './App.css';
import Field from './model/Field'
import Desk from './model/Desk'
function App() {
  return (
    <div className="container">
      <div className="chess">
        <Desk />
      </div>
    </div>    

    );
}

export default App;
