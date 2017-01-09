function numberText = convertNumberToString(number, numberOfDigits)
% convertNumberToString Takes a number as a parameter and return it as a string

% Initialize texts
numberText = '';
reversedNumberText = '';

% Initially, number will be reversed
while(number != 0)
	reversedNumberText = [reversedNumberText char(mod(number,10)+48)];
	number = floor(number/10);
end

sz = size(reversedNumberText,2);
% Add leading zeros
for i = 1:numberOfDigits - sz
	numberText = [numberText '0'];
end

% Reverse text back
for i = 1:sz
	numberText = [numberText reversedNumberText(1,sz-i+1)];
end

end
