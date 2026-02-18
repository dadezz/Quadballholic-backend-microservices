@echo off
echo ==================================================
echo BUILDING ALL SERVICES
echo ==================================================

echo.
echo [1/11] Building Reservation Service...
docker build -f reservation/Dockerfile -t ec2m/quadball-microservice-reservation:latest .

echo.
echo [2/11] Building Discovery Server...
docker build -f discoveryServer/Dockerfile -t ec2m/quadball-microservice-discovery_server:latest .

echo.
echo [3/11] Building Live Game Events...
docker build -f live-game-events/Dockerfile -t ec2m/quadball-microservice-live_game_events:latest .

echo.
echo [4/11] Building Match Service...
docker build -f match/Dockerfile -t ec2m/quadball-microservice-match:latest .

echo.
echo [5/11] Building Player Service...
docker build -f player/Dockerfile -t ec2m/quadball-microservice-player:latest .

echo.
echo [6/11] Building Team Service...
docker build -f team/Dockerfile -t ec2m/quadball-microservice-team:latest .

echo.
echo [7/11] Building Match Official Service...
docker build -f match_official/Dockerfile -t ec2m/quadball-microservice-match-official:latest .

echo.
echo [8/11] Building User Auth Service...
docker build -f userAuth/Dockerfile -t ec2m/quadball-microservice-user_auth:latest .

echo.
echo [9/11] Building Tournaments Service...
docker build -f tournaments/Dockerfile -t ec2m/quadball-microservice-tournaments:latest .

echo.
echo [10/11] Building API Gateway...
docker build -f apiGateway/Dockerfile -t ec2m/quadball-microservice-api-gateway:latest .

echo.
echo [11/11] Building Stadium Service...
docker build -f stadium/Dockerfile -t ec2m/quadball-microservice-stadium:latest .

echo.
echo ==================================================
echo ALL SERVICES ARE BUILT
echo ==================================================
pause