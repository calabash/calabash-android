#!/bin/bash
java -cp build-libs/antlr-3.4-complete.jar org.antlr.Tool antlr/UIQuery.g -o src/sh/calaba/instrumentationbackend/query
