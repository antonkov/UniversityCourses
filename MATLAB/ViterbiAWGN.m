s = RandStream.create('mt19937ar', 'seed',94384);
prevStream = RandStream.setGlobalStream(s);
msg = randi([0 1],1000000,1); % Random data
names = ['(5 2 7)'; '(6 4 3)'; '(5 5 7)'; '(7 5 1)'];
tests = [[5 2 7]; [6 4 3]; [5 5 7]; [7 5 1]];
kfsSet = [[1 4 12 32 80 192 448 1024 2304 5120];
    [1 2 3 4 5 8 13 20 29 40];
    [1 2 3 6 14 30 57 102 181 324];
    [1 4 12 32 80 192 448 1024 2304 5120]];
pwsSet = [[6 8 10 12 14 16 18 20 22 24];
    [5 6 7 8 9 10 11 12 13 14];
    [7 8 9 10 11 12 13 14 15 16];
    [6 8 10 12 14 16 18 20 22 24]];
testId = 4;
kfs = kfsSet(testId, :);
pws = pwsSet(testId, :);
gens = tests(testId,:);
t = poly2trellis(3,gens); % Define trellis.
spect = distspec(t, 10);
df = spect.dfree;
spect.weight
% Create a ConvolutionalEncoder System object
hConvEnc = comm.ConvolutionalEncoder(t);
n = 40;
xs = linspace(1, 2, n);
xsIndB = 10 * log10(xs);
ratio = zeros(n, 1);
number = zeros(n, 1);
pebsAWGN = zeros(n, 1);
pebsBSC = zeros(n, 1);
pebsAWGN10 = zeros(n, 1);
pebsBSC10 = zeros(n, 1);
pebsAWGN5 = zeros(n, 1);
pebsBSC5 = zeros(n, 1);
for i=1:n
    x = xs(i);
    b = (1 - normcdf(sqrt(2 * df * x))) * exp(df * x);
    d0 = exp(-x);
    pebsAWGN(i) = b * F(d0);
    pebsAWGN10(i) = b * pseudoF(d0, kfs, pws, 10);
    pebsAWGN5(i) = b * pseudoF(d0, kfs, pws, 5);
    
    p = 1 - normcdf(sqrt(2 * x));
    b = (1 - p) / (1 - 2 * p) * sqrt(2 / (df * pi));
    d0 = 2 * sqrt(p * (1 - p));
    pebsBSC(i) = b * F(d0);
    pebsBSC10(i) = b * pseudoF(d0, kfs, pws, 10);
    pebsBSC5(i)  = b * pseudoF(d0, kfs, pws, 5);
    
    % Create an AWGNChannel System object.
    hChan = comm.AWGNChannel('NoiseMethod', 'Signal to noise ratio (Eb/No)',...
      'EbNo', xsIndB(i));
    tblen = 48; 
    % Create a ViterbiDecoder System object
    hMod = comm.QPSKModulator('BitInput',true);
    hDemodLLR = comm.QPSKDemodulator('BitOutput',true,...
        'DecisionMethod', 'Log-likelihood ratio');
    hVitDec = comm.ViterbiDecoder(t, 'TracebackDepth', tblen, 'TerminationMethod', 'Continuous');
    %hVitDec = comm.ViterbiDecoder(t, 'InputFormat', 'Soft', ...
    %    'SoftInputWordLength', 3, 'TracebackDepth', tblen, ...
    %    'TerminationMethod', 'Continuous');
    
    % Create a ErrorRate Calculator System object. Account for the receive
    % delay caused by the traceback length of the viterbi decoder.
    hErrorCalc = comm.ErrorRate('ReceiveDelay', tblen);
    code = step(hConvEnc,msg); % Encode the data.
    hChan.SignalPower = (code'*code)/length(code);
    code = step(hMod, code);
    ncode = step(hChan,code); % Add noise.
    ncode = step(hDemodLLR,ncode);
    % Quantize to prepare for soft-decision decoding.
    %qcode = bsc(code, p);
    qcode = ncode;
    %qcode = quantiz(ncode,[0.001,.1,.3,.5,.7,.9,.999]);

    delay = tblen;
    decoded = step(hVitDec,qcode); % Decode.

    % Compute bit error rate.
    ber = step(hErrorCalc, msg, decoded);
    ratio(i) = ber(1);
    number(i) = ber(2);
end;
figure;
semilogy(xsIndB, ratio, 'DisplayName', 'Viterbi simulation');
hold on;
semilogy(xsIndB, pebsAWGN, 'DisplayName', 'AWGN with all terms of F(D)');
hold on;
semilogy(xsIndB, pebsAWGN10, 'DisplayName','AWGN with 10 terms of F(D)');
hold on;
semilogy(xsIndB, pebsAWGN5, 'DisplayName', 'AWGN with 5 terms of F(D)');
legend('show')
title(names(testId, :));
xlabel('E_b/No dB');
ylabel('P_{eb}');
hold off;
format long;
log10(ratio)
%pebs
RandStream.setGlobalStream(prevStream);