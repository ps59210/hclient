# Running hbench tool in batch and aggregating multiple runs

## BatchRun

This file runs multiple hbench tool in parallel or sequential order based on the inputs.
Following are the important parameters which are then used in triggering hbench:


| Param                | Description                                 | Type | Example value |
|----------------------|---------------------------------------------|------|---------------|
| "-t", "--numThreads" | list of threads which goes as input to hbench in --threads | comma seperated string | 10,50,100          |  
| "-c", "--numCols" | list of columns which goes as input individually to hbench in --columns | comma seperated string | 50,1000,2000          |  
| "-p", "--numPart" | list of partitions which goes as input to hbench tool under --params | comma seperated string | 100,500          |  
| "-i", "--numInstances" | list of object params for hbench under -N, hbench runs api against each input | comma seperated string | 100,1000       |  
| "-o", "--order" | either 'ascending' or 'descending' based on it the above parameters are given to hbench tool in order, if its max then only the max values will be sent to hbench| string | ascending/descending/max          |  
| "-s", "--script" | this is the common script values for hbench tool with the apis , it is first split from -M and then used | string | --savedata /tmp/benchdata --sanitize -CR -M 'addPartition.*'          |  
| "-l", "--capacitytag" | This is the capacity tag for the hbench tool, based on this the database name is created it has to be unique in every run and identifies the capacity and load params | string | 8GBH_20P_01I_AddPartitions          |  
| "-r", "--run" | Identifies one run against capacity and load params, the results will be aggregated against this run | string | a          |  
| "-u", "--hosturi" | the host towards which the apis are triggered, if there are two then the spin count is divided by 2 and sent to each host in parallel | comma seperated string | dbrscale-w5f1fv-gateway0.dbrscale.svbr-nqvp.int.cldr.work,dbrscale-w5f1fv-gateway1.dbrscale.svbr-nqvp.int.cldr.work           |  
| "-k", "--krandom" | if this random is true then for each input under -M a sperate instance of hbench will be triggred but the results will be aggregated against run_tag | string | "1" to enable and "0" to disable         |  
| "-d", "--dir" | directory at which hbench needs to be run, this is where all the bin/hbench is present | direcotry path string | -d /Users/prateek.singh/Project/hclient        |  

This file can be run from your mac to see the varous combinations of input to hbench:
For example:


| Command with max             |
|----------------------|
| python3 BatchRun.py -u dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work,dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work -r c -t 15,30,50 -c 101,501 -p 100,1000 -i 100 -o max -l 10P_2I_17179869184H_test168 -s "--savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.*" -k 0 -d /Users/prateek.singh/Project/hclient |
| Multiple command combinations - uses just the maximum value             |
|----------------------|
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |




| Command with ascending             |
|----------------------|
| python3 BatchRun.py -u dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work -r c -t 15,30,50 -c 101,501 -p 100,1000 -i 100 -o ascending -l 10P_2I_17179869184H_test168 -s "--savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.*" -k 0 -d /Users/prateek.singh/Project/hclient |
| Multiple command combinations for ascending - note ascending order of each threads           |
|----------------------|
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 30 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 100 -N 100  -o Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 1000 -N 100  -o Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |



| Command with ascending and multiple hosts            |
|----------------------|
| python3 BatchRun.py -u dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work,dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work -r c -t 15,30,50 -c 101,501 -p 100,1000 -i 100 -o ascending -l 10P_2I_17179869184H_test168 -s "--savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.*" -k 0 -d /Users/prateek.singh/Project/hclient |
| Multiple command combinations for ascending - note hbench tool for each host and different spin count and thread count          |
|----------------------|
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#1.csv -d Runc0_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#1.csv -d Runc1_30threads_101columns_100params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 25 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#1.csv -d Runc2_50threads_101columns_100params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 25 |
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 100 -N 100  -o Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 100 -N 100  -o Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#1.csv -d Runc3_15threads_501columns_100params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 1000 -N 100  -o Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 1000 -N 100  -o Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#1.csv -d Runc4_15threads_101columns_1000params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 7 |
| bin/hbench -H dbrscale-s3wcoy-gateway0.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 50 --threads 25 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#1.csv -d Runc5_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#1 --spin 50 --threads 25 |





| Command with descending             |
|----------------------|
| python3 BatchRun.py -u dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work -r c -t 15,30,50 -c 101,501 -p 100,1000 -i 100 -o descending -l 10P_2I_17179869184H_test168 -s "--savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.*" -k 0 -d /Users/prateek.singh/Project/hclient |
| Multiple command combinations for ascending - note descending order of each threads           |
|----------------------|
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc0_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc0_50threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc1_30threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc1_30threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 30 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 1000 -N 100  -o Runc2_15threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc2_15threads_501columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 15 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 1000 -N 100  -o Runc3_50threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0.csv -d Runc3_50threads_101columns_1000params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 501 --params 100 -N 100  -o Runc4_50threads_501columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc4_50threads_501columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 50 |
| bin/hbench -H dbrscale-s3wcoy-gateway1.dbrscale.svbr-nqvp.int.cldr.work --savedata /tmp/benchdata --sanitize -CR -M addPartition.* -M dropPartition.* -M getPartition.* -M listPartition.* --columns 101 --params 100 -N 100  -o Runc5_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0.csv -d Runc5_15threads_101columns_100params_100N_10P_2I_17179869184H_test168#0 --spin 100 --threads 15 |


 
 ## ResultAggregator
 
The result aggregator aggregates all the csv output files present in current folder and clubs them into master.csv
It contains the file names under tag column in master.csv