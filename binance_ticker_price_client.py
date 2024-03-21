import requests


class BinanceTickerPriceClient:
    TICKER_PRICE_URL = "https://api.binance.com/api/v3/ticker/price"

    def __init__(self, symbols):
        self.symbols = symbols

    def _build_query_params(self):
        symbol_param = '["' + '","'.join(self.symbols) + '"]'
        return {"symbols": symbol_param}

    def get_ticker_prices(self):
        try:
            params = self._build_query_params()
            response = requests.get(self.TICKER_PRICE_URL, params=params)

            if response.status_code == 200:
                return response.json()
            else:
                response.raise_for_status()
                
        except requests.exceptions.RequestException as e:
            print(f"Error connecting to Binance API: {e}")
            return None
    