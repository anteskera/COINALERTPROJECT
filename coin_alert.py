from binance_ticker_price_client import BinanceTickerPriceClient
from mail_agent import MailAgent
from datetime import datetime as dt
from tabulate import tabulate


def create_email_body(ticker_prices):
    ticker_prices_array = [
        [d["symbol"], float(d["price"])] 
        for d in ticker_prices
    ]

    load_timestamp = dt.now().replace(second=0, microsecond=0)

    email_body_ticker_table = tabulate(
        ticker_prices_array,
        headers=["Coin name", "Price"],
        tablefmt="simple",
    )

    email_body = (
        f"Hello, \n"
        + f"below are the prices actual at {load_timestamp}\n"
        + email_body_ticker_table
    )

    return email_body


if __name__ == "__main__":
    # Example Usage
    symbols = [
        "SUPERUSDT",
        "PYRUSDT",
        "ETHUSDT",
        "ADAUSDT",
        "BTCUSDT",
        "FTMUSDT",
        "SOLUSDT",
        "MOVRUSDT",
        "MATICUSDT",
    ]

    binance_ticker_client = BinanceTickerPriceClient(symbols)
    ticker_prices = binance_ticker_client.get_ticker_prices()
    email_body = create_email_body(ticker_prices)

    MailAgent.send_email("Crypto prices", email_body)
