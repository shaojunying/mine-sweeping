#!/usr/bin/env bash
# 判断out目录是否存在，存在就清空，不存在则创建
DIR="out"
if [ -d "$DIR" ]; then
  # 文件夹存在 清空文件夹的内容
  rm -rf "$DIR"
  echo "成功删除out目录"
fi
mkdir out
echo "成功创建out目录"

# 编译Java文件
javac -d out src/**/*.java
echo "成功编译Java文件"

# 运行项目
java -cp out shao.MineSweeping
echo "成功运行项目"
