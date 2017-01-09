function p = predict(Theta1, Theta2, X, mode)
%PREDICT Predict the label of an input given a trained neural network
%   p = PREDICT(Theta1, Theta2, X, mode) outputs the predicted label of X given the
%   trained weights of a neural network (Theta1, Theta2)
%   mode 1 = number, 2 = alphabet

% Useful values
m = size(X, 1);
num_labels = size(Theta2, 1);
labelRange = [1 10; 11 36;1 2];

% You need to return the following variables correctly 
p = zeros(size(X, 1), 1);

h1 = sigmoid([ones(m, 1) X] * Theta1');
h2 = sigmoid([ones(m, 1) h1] * Theta2');
%for i=1:26
%	printf("%c: %.2f\n",i+64,h2(i));
%end
[dummy, p] = max(h2, [], 2);

% =========================================================================


end
