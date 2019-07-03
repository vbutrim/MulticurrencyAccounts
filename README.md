# Multicurrency Accounts

RESTful API for money transfers between accounts

## Idea

Bank clients don't know anything about internal mapping in storage of their private data.
As a Client I would like to transfer some money to another Client's account. From this point of view, I know Client's name, to whom I would like to transfer, currency of transferring and its amount.
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