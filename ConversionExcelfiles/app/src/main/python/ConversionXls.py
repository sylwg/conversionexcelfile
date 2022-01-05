import pandas as pd
import time


def xlsx_to_csv_pd(src_File:str,tag_File:str):
    data_xls = pd.read_excel(src_File, index_col=0)
    data_xls.to_csv(tag_File, encoding='utf_8_sig')