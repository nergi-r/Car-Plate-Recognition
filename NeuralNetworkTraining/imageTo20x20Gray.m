function vectorImage = imageTo20x20Gray(fileName, cropPercentage=0, rotStep=0, ...
										initBackground, height, width)
%IMAGETO20X20GRAY display reduced image and converts for digit classification
%
% Sample usage: 
%       convertImage('myDigit.jpg', 100, -1);
%
%       First parameter: Image file name
%             Could be bigger than height x width px, it will
%             be resized to height x width. Better if used with
%             square images but not required.
% 
%       Second parameter: cropPercentage (any number between 0 and 100)
%             0  0% will be cropped (optional, no needed for square images)
%            50  50% of available croping will be cropped
%           100  crop all the way to square image (for rectangular images)
% 
%       Third parameter: rotStep
%            -1  rotate image 90 degrees CCW
%             0  do not rotate (optional)
%             1  rotate image 90 degrees CW
%
%		Fourth parameter: initBackground (initial background color (white/black))
%			  1 Number is black and background is white (Must be inverted)			
%			  0 Number is white and background is black (No invertion needed)
%
% (Thanks to Edwin Frühwirth for parts of this code)

% Check whether image is rgb or not
im = imread(fileName);

% Check whether image blank (Only black color)
maxValue = max(im(:));
minValue = min(im(:));
% Compute the value range of actual grays
delta = maxValue - minValue;

if(size(im,3)==1 || delta<200) rgb = 0;
else rgb = 1;
end

if(rgb)
	% Read as RGB image
	Image3DmatrixRGB = imread(fileName);
	% Convert to NTSC image (YIQ)
	Image3DmatrixYIQ = rgb2ntsc(Image3DmatrixRGB);
	% Convert to grays keeping only luminance (Y) and discard chrominance (IQ)
	Image2DmatrixBW  = Image3DmatrixYIQ(:,:,1);
else
	% Read as BW image
	Image2DmatrixBW = imread(fileName);
	Image2DmatrixBW = Image2DmatrixBW(:,:,1);
	% Convert to range [0 1]
	Image2DmatrixBW = double(Image2DmatrixBW) ./ 255.0;
end
% Get the size of your image
oldSize = size(Image2DmatrixBW);
% Obtain crop size toward centered square (cropDelta)
% ...will be zero for the already minimum dimension
% ...and if the cropPercentage is zero, 
% ...both dimensions are zero
% ...meaning that the original image will go intact to croppedImage
cropDelta = floor((oldSize - min(oldSize)) .* (cropPercentage/100));
% Compute the desired final pixel size for the original image
finalSize = oldSize - cropDelta;
% Compute each dimension origin for croping
cropOrigin = floor(cropDelta / 2) + 1;
% Compute each dimension copying size
copySize = cropOrigin + finalSize - 1;
% Copy just the desired cropped image from the original B&W image
croppedImage = Image2DmatrixBW( ...
                    cropOrigin(1):copySize(1), cropOrigin(2):copySize(2));
% Resolution scale factors: [rows cols]
scale = [height width] ./ finalSize;
% Compute back the new image size (extra step to keep code general)
%newSize = max(floor(scale .* finalSize),1); 
newSize = [height width];
% Compute a re-sampled set of indices:
rowIndex = min(round(((1:newSize(1))-0.5)./scale(1)+0.5), finalSize(1));
colIndex = min(round(((1:newSize(2))-0.5)./scale(2)+0.5), finalSize(2));
% Copy just the indexed values from old image to get new image
newImage = croppedImage(rowIndex,colIndex,:);
% Rotate if needed: -1 is CCW, 0 is no rotate, 1 is CW
newAlignedImage = rot90(newImage, rotStep);

if(initBackground==1)
	% Invert black and white
	invertedImage = - newAlignedImage;
else
	invertedImage = newAlignedImage;
end
% Find min and max grays values in the image
maxValue = max(invertedImage(:));
minValue = min(invertedImage(:));
% Compute the value range of actual grays
delta = maxValue - minValue;
% Empty file checker
if(delta==0)
	delta = 1;
end
% Normalize grays between 0 and 1
normImage = (invertedImage - minValue) / delta;
% Add contrast. Multiplication factor is contrast control.
contrastedImage = sigmoid((normImage -0.5) * 5);
% Show image as seen by the classifier
% imshow(contrastedImage, [-1, 1] );
% Output the matrix as a unrolled vector
vectorImage = reshape(contrastedImage, 1, newSize(1)*newSize(2));
end