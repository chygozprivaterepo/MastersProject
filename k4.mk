JFLAGS  = -g
JC = javac
JVM = java
SHELL := /bin/bash
INSTANCES := $(shell cut -d' ' -f1 instances.txt)
RESULTSDIR := results
PATTERNDIR := efficient_k4_detection_result
PATTERNALG := efficientdetection/DetectK4

default: $(foreach i, $(INSTANCES), $(RESULTS)/$(PATTERN)/$(i))

$(RESULTSDIR)/$(PATTERNDIR)/%:
	mkdir -p $(RESULTSDIR)$(PATTERNDIR)
	$(JVM) efficientdetection $(PATTERNALG) $* | tee $(RESULTSDIR)/$(PATTERNDIR)/$*
