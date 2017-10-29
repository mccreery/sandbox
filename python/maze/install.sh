#!/bin/bash

uflash main.py

for file in lvl*.txt
do
	ufs put $file
done
