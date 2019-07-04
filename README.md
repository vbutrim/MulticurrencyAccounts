# Multicurrency Accounts

RESTful API for money transfers between accounts

## Idea

Bank clients don't know anything about internal mapping in storage of their private data into database's ids, so transferring like (1 (acc1)) --> 2 (acc2) 45 (amount)) says nothing.
_As a Client_ I would like to _transfer some money_ to another _Client's account_.  
From this point of view, I know Client's name, to whom I would like to transfer, currency of transferring and amount of Negotiation.
As an example:
```$xslt
   CLIENT_NAME_FROM    CLIENT_NAME_TO    CURRENCY_OF_TRANSFER    AMOUNT
   ####################################################################
1.  Antonio Bandera      Salma Hayek              EUR              100
```

It means that internal developing system has to know about Clients and its Accounts: Currencies (CCY) and Balance.

Client has Name, Accounts and other relevant information (Passport Id, e.g.).  
Each Account has Owner (Client), Currency, Balance.  
As a Client I can withdraw cash from my account, top up it (with ATM, e.g.) and transfer money from it to another account (Matching of CCYs is important).

## Architecture

![Archi](https://user-images.githubusercontent.com/17473868/60666388-9a5f3500-9e6f-11e9-81cf-327db9b66f57.png)

## How to run

Firstly, build last version of executable jar by 2 variants depending on your system.

Windows-like:
```$xslt
> assembly.bat
```

*nix-like:
```$xslt
> assembly.sh
```

To run the server:
```$xslt
> java -jar server.jar [PORT=8080]
```

## API usage

**Available Currencies**
```aidl
    USD("USD")
    EUR("EUR")
    GBP("GBP")
    RUB("RUB")
```

___

**Create new Client and Account with certain Currency**
```aidl
POST http://localhost:PORT/api/v1/clients

Input ClientRequestDto:
{
    "name": "Warren Buffett",
    "passportId": "30august1930",
    "ccyOfInitialAccount": "USD"        // optional: Default = "EUR"
}

Output String:
    "Client 'Warren Buffet' with id '1' and 'USD' account were successfully created"

```

**Get list of all Clients with opened Accounts (Currency -- AccountId)**
```aidl
GET http://localhost:PORT/api/v1/clients

Output Stringified JSON:
[
    {
        "id": 1,
        "name": "Warren Buffet",
        "passportId": "30august1930",
        "accountIdPerCcy": {
            "USD": 1,
            "EUR": 2
        }
    },
    {
        "id": 2,
        "name": "Steve Jobs",
        "passportId": "24february1955",
        "accountIdPerCcy": {
            "EUR": 3
        }
    }
]
```

**Get certain Client information by ClientId**
```aidl
GET http://localhost:PORT/api/v1/clients/1

Output Stringified JSON:
{
    "id": 1,
    "name": "Warren Buffet",
    "passportId": "30august1930",
    "accountIdPerCcy": {
        "USD": 1,
        "EUR": 2
    }
}
```

**Close Client's accounts and remove him from database**  
_**Warning**: works only if total balance of accounts is 0_

```aidl
DELETE http://localhost/api/v1/clients/1

Output Stringified JSON:
    "Account(s) of Client with id 1 has(-ve) been successfully closed"

```
___

**Create new Account with certain Currency for Client**
```aidl
POST http://localhost:PORT/api/v1/accounts

Input AccountRequestDto:
{
	"clientName": "Warren Buffet",
	"currency": "EUR"
}

Output String:
    "'EUR' account with id '2' for Client 'Warren Buffet' was successfully created"
```

**Get Account's information of Client**
```aidl
GET http://localhost:PORT/api/v1/accounts?clientName=Tony&currency=EUR

Input params: "clientName", "currency"

Output Stringified JSON:
{
    "id": 2,
    "ccy": "EUR",
    "balance": 0
}
```

___

**Withdraw cash & Top Up Balance of Client's Account**
```aidl
PUT http://localhost:PORT/api/v1/transfer

Available Actions:
    WITHDRAW("WITHDRAW")
    TOP_UP("TOP_UP")


Input ExtendedAccountRequestDto:
{
	"clientName": "Warren Buffet",
	"currency": "EUR",
	"action": "TOP_UP",
	"amount": 100
}

Output String:
    "'Warren Buffet's account: Successfully top up 100 EURs"
```

**Transfer money between Clients' accounts**
```aidl
POST http://localhost:PORT/api/v1/transfer

Input TransferRequestDto:
{
	"clientNameFrom": "Warren Buffet",
	"clientNameTo": "Steve Jobs",
	"currency": "EUR",
	"amountMoney": 10
}

Output String:
    "SUCCESSFULLY TRANSFERRED"
```

**Get all history of successful Transactions (transfer, top up, withdraw)**
```aidl
GET http://localhost:PORT/api/v1/transfer

Output Stringified JSON:
[
    {
        "id": 1,
        "action": "TOP_UP",
        "clientNameFrom": "Warren Buffet",
        "clientNameTo": "<null>",
        "currencyOfOperation": "EUR",
        "amount": 100
    },
    {
        "id": 2,
        "action": "TRANSFER",
        "clientNameFrom": "Warren Buffet",
        "clientNameTo": "Steve Jobs",
        "currencyOfOperation": "EUR",
        "amount": 10
    }
]
```