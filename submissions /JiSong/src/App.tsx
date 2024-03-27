import { useState, useEffect } from 'react';
import './App.scss';
import rustLogo from './assets/rust.svg';
import reactLogo from './assets/react.svg';
import ethLogo from './assets/eth.svg';
import { backend } from './declarations/backend';
import { Block } from './declarations/backend/backend.did';

// JSON viewer component
import { JsonView, allExpanded, defaultStyles } from 'react-json-view-lite';
import 'react-json-view-lite/dist/index.css';

function App() {
  const [loading, setLoading] = useState(false);
  const [block, setBlock] = useState<Block | undefined>();
  console.log(block)
  const [error, setError] = useState<string | undefined>();
  const [blocks, setBlocks] = useState<Block[]>([]);

  const [sel, setSel] = useState(-1)

  useEffect(()=>{
    setInterval(()=>{
      fetchBlock();
    }, 5000)
  }, [])

  useEffect(()=>{
    if(block){
      let has = false
      blocks.forEach((data : any)=>{
        if(data.receiptsRoot === block.receiptsRoot){
          has = true
        }
      })
      if(!has){
        let b = blocks.slice()
        b.push(block)
        setBlocks(b)
      }
    }
  }, [block])

  const fetchBlock = async () => {
    try {
      setLoading(true);
      setError(undefined);
      const block = await backend.getLatestEthereumBlock();
      //const block = await backend.getEthereumBlockByNumber();
      setBlock(block);
    } catch (err) {
      console.error(err);
      setError(String(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <h1 style={{ paddingLeft: 36 }}>Crosschain Scan</h1>
      <h3 style={{ paddingLeft: 36 }}>currentMode : ETH</h3>
      <div className="card" style={{boxShadow: "5px 5px 10px", height: "40rem", overflow: 'auto'}}>
        <div style={{fontWeight: 'bold', marginBottom: 20, display: 'grid', gridTemplateColumns: "repeat(3, minmax(0, 1fr))"}}>
          <div>Block #</div>
          <div># of Transactions</div>
          <div>Miner</div>
        </div>
        {blocks.map((item: Block, index: number)=>{return <>
        <div style={{display: 'grid', gridTemplateColumns: "repeat(3, minmax(0, 1fr))", marginBottom: 10}}>
          <div>{Number(item.number)}</div>
          <div onClick={()=>{if(sel === index) setSel(-1) 
            else setSel(index)}} style={{textDecoration: 'underline', color: 'blue', cursor: 'pointer'}}>{Number(item.transactions.length)}</div>
          <div>{item.miner}</div>
        </div>
        {sel === index && <div>
            {item.transactions.map((item: any)=>{
              return <div>{item}</div>
            })}
        </div>}
        </>})}
      </div>
    </div>
  );
}

export default App;
