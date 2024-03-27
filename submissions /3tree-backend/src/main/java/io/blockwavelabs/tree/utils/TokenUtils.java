package io.blockwavelabs.tree.utils;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.http.HttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class TokenUtils {
    @Value("${block-chain.infura.project-endpoint.goerli}")
    private String goerliEndPoint;
    @Value("${block-chain.metamask.private-key.office}")
    private String privateKey;
    @Value("${block-chain.goerli.gas-fee-estimate.api-key}")
    private String goerliGasFeeEndPoint;

    public String goerliEthTransfer(String toAddress, float amountInEther) throws Exception {
        // Connect to the Goerli testnet
        Web3j web3j = Web3j.build(new HttpService(goerliEndPoint));
        Credentials credentials = Credentials.create(privateKey);

        // Set the parameters for the transfer
        String fromAddress = credentials.getAddress();
        BigDecimal amountinEthBD = BigDecimal.valueOf(amountInEther);
        BigInteger amountInWei = Convert.toWei(amountinEthBD, Convert.Unit.ETHER).toBigInteger();
        BigInteger gasPrice  =  estimateGasFeeinWei();
        BigInteger gasLimit = BigInteger.valueOf(21_000);
        BigInteger nonce = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.LATEST).send().getTransactionCount();

        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(nonce, gasPrice, gasLimit, toAddress, amountInWei);
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

        // Print the transaction hash
        System.out.println("TrxHash:       " + ethSendTransaction.getTransactionHash());
        if (ethSendTransaction.hasError()) {
            System.out.println("Transaction failed: " + ethSendTransaction.getError().getMessage());
            return "FAILED";
        }
        return ethSendTransaction.getTransactionHash();
    }

    public String goerliEthTransferCheck(String trxHash) throws Exception {
        Web3j web3j = Web3j.build(new HttpService(goerliEndPoint));
        Transaction transaction = web3j.ethGetTransactionByHash(trxHash).send().getResult();

        if (transaction != null) {
            try{
                if(transaction.getBlockNumber() !=null){
                    System.out.println(transaction.getBlockNumber());
                    return "SUCCESS";
                }else{
                    return "PENDING";
                }
            }catch(Exception e){
                return "PENDING";
            }
        }
        return "FAILED";
    }

    public HashMap<String, Float> getGasFee(String trxHash) throws IOException {
        HashMap<String,Float> gasMap = new HashMap<String,Float>();
        Web3j web3j = Web3j.build(new HttpService(goerliEndPoint));

        TransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(trxHash).send().getResult();
        Transaction transaction = web3j.ethGetTransactionByHash(trxHash).send().getResult();

        BigInteger gasUsed = transactionReceipt.getGasUsed();
        BigInteger gasPrice = transaction.getGasPrice();
        BigInteger gasLimit = transaction.getGas();
        BigInteger transactionFee = gasUsed.multiply(gasPrice);
        //BigInteger burntFee = gasLimit.subtract(gasUsed).multiply(gasPrice);

        //BigDecimal gasUsedInGwei = new BigDecimal(gasUsed).divide(BigDecimal.valueOf(1000000000));
        //BigDecimal gasPriceInGwei = new BigDecimal(gasPrice).divide(BigDecimal.valueOf(1000000000));
        //BigDecimal gasLimitInGwei = new BigDecimal(gasLimit).divide(BigDecimal.valueOf(1000000000));
        BigDecimal transactionFeeInGwei = new BigDecimal(transactionFee).divide(BigDecimal.valueOf(1000000000));
        //BigDecimal burntFeeInGwei = new BigDecimal(burntFee).divide(BigDecimal.valueOf(1000000000));

        gasMap.put("gasUsedInWei", gasUsed.floatValue());
        gasMap.put("gasPriceInWei", gasPrice.floatValue());
        gasMap.put("gasLimitInWei", gasLimit.floatValue());
        gasMap.put("transactionFeeInGwei", transactionFeeInGwei.floatValue());

        System.out.println(gasMap);

        return gasMap;
    }

    public BigInteger estimateGasFeeinWei() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(goerliGasFeeEndPoint, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        //int status = httpStatus.value();
        String response = responseEntity.getBody();
        JSONObject jsonObject= (JSONObject)jsonParser.parse(response);

        JSONArray jsonArrRes = (JSONArray)jsonObject.get("speeds");
        System.out.print("GasFee api result: ");
        System.out.println((JSONObject)jsonArrRes.get(2));

        Double number = (Double)((JSONObject) jsonArrRes.get(2)).get("baseFee") * Math.pow(10, 9);
        BigDecimal bd = BigDecimal.valueOf(number);
        BigInteger bi = bd.toBigInteger();

        return bi;
    }


}
