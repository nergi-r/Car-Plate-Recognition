clear;

% Setup layer size;
input_layer_size = 28^2; % Input is image matrix with size 28x28
num_labels = 10; % Output from [0-9]
datasets = 1016; % Number of datasets

% Convert image datasets to matrix datasets
filename = "numberDatasets.txt";
fid = fopen(filename,"w");

% Parse images datasets to matrix datasets
dataCounter = 1;
base = 'EnglishFnt\English\Fnt\Sample0';
for i = 1:num_labels
	labelText = convertNumberToString(i, 2);
	baseSampleLabel = [base labelText '\'];
	for j = 1:datasets
		baseFileName = [baseSampleLabel convertNumberToString(j, 5) '.png'];
		
		sample = imread(baseFileName);
		sample = sample(:,:,1);
		height = 28;
		width = 28;
		finalSize = size(sample);
		scale = [height width] ./ finalSize;
		newSize = [height width];
		rowIndex = min(round(((1:newSize(1))-0.5)./scale(1)+0.5), finalSize(1));
		colIndex = min(round(((1:newSize(2))-0.5)./scale(2)+0.5), finalSize(2));
		newImage = sample(rowIndex,colIndex,:);

		mx = max(newImage(:));
		limit = 0;
		if(mx==1) limit = 0;
		else limit = 150;
		end
		X2 = 0;
		for i = 1:size(newImage,1)
			for j = 1:size(newImage,2)
				X2(i,j) = newImage(i,j);
				if(X2(i,j) > limit) X2(i,j) = 0;
				else X2(i,j) = 255;
				end
			end
		end
		X2 = double(X2) ./ 255;
		X2 = reshape(X2,1,784);


		for k = 1:input_layer_size
			fprintf(fid,"%d ",X2(1,k));
		end
		fprintf(fid,"\n");
		dataCounter++;
	end
end

fclose(filename);

% Create datasets labels
filename = "numberLabels.txt";
fid = fopen(filename,"w");
for i = 1:num_labels
	for j = 1:m
		fprintf(fid,"%d\n",mod(i,10));
	end
end
fclose(filename);

% Initializations
clear; close all;

% Setup layer size
input_layer_size = 28^2; % Input is image matrix with size 28x28 
hidden_layer_size = 200; % Number of neurons in hidden layer
num_labels = 10; % Output from [0-9]
datasets = 1016; % Number of datasets

% Load first datasets
m = datasets * num_labels;
X = load('numberDatasets.txt');
y = load('numberLabels.txt');

% Setup parameters
lambda = 1.2;
iteration = 1000;

% Initialize random weights
initial_Theta1 = randInitializeWeights(input_layer_size, hidden_layer_size);
initial_Theta2 = randInitializeWeights(hidden_layer_size, num_labels);

% Unroll parameters
initial_nn_params = [initial_Theta1(:) ; initial_Theta2(:)];
clear initial_Theta1 initial_Theta2;

% Train Neural Network
options = optimset('MaxIter', iteration);

% Create "short hand" for the cost function to be minimized
costFunction = @(p) nnCostFunction(p, ...
                                   input_layer_size, ...
                                   hidden_layer_size, ...
                                   num_labels, X, y, lambda);

% costFunction is a function that takes in only one argument (the
% neural network parameters)
[nn_params, cost] = fmincg(costFunction, initial_nn_params, options);

% Obtain Theta1 and Theta2 back from nn_params
Theta1 = reshape(nn_params(1:hidden_layer_size * (input_layer_size + 1)), ...
                 hidden_layer_size, (input_layer_size + 1));

Theta2 = reshape(nn_params((1 + (hidden_layer_size * (input_layer_size + 1))):end), ...
                 num_labels, (hidden_layer_size + 1));

filename = "numberWeights.txt";
fid = fopen(filename,"w");
for i = 1:size(nn_params,1);
	fprintf(fid,"%d ",nn_params(i,1));
end
fprintf(fid,"\n");
fclose(filename);

% Save params and weights
filename = "parameters.txt";
fid = fopen(filename,"w");
fprintf(fid,"input_layer_size: %d\n",input_layer_size);
fprintf(fid,"hidden_layer_size: %d\n",hidden_layer_size);
fprintf(fid,"num_labels: %d\n",num_labels);
fprintf(fid,"lambda: %f\n",lambda);
fprintf(fid,"iteration: %d\n",iteration);
fclose(filename);
