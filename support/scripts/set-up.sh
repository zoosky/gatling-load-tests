#!/bin/bash

# This is a set-up script for running Gatling on a BBC CentOS build.

echo "Goto HOME"
cd

echo "download SBT 0.13.7 RPM"
wget "https://dl.bintray.com/sbt/rpm/sbt-0.13.7.rpm"

echo "Installing SBT RPM"
sudo yum install -y "sbt-0.13.7.rpm"

echo "install Vim pathogen"
mkdir -p ~/.vim/autoload ~/.vim/bundle && \
  curl -LSso ~/.vim/autoload/pathogen.vim https://tpo.pe/pathogen.vim

echo "Vim plugins"
cd ~/.vim/bundle
git clone https://github.com/derekwyatt/vim-scala
git clone https://github.com/kien/ctrlp.vim
git clone https://github.com/tpope/vim-commentary

echo "Configure Vim" 
cat << EOF > ~/.vimrc
execute pathogen#infect()
syntax on
filetype plugin indent on
set shortmess+=I
set wildignore+=*/target/*
EOF

echo "install netcat"
sudo yum install -y nc.x86_64

echo "Configure git"
git config --global alias.st status
git config --global color.ui true

echo "Some Bash"
cat << EOF > ~/.bashrc 
alias ..="cd .." 
alias cl='clear'
alias vi='vim' 
export PS1="\u@\h \w "
EOF

echo "GNU Screen"
sudo yum install -y screen

exit 0
# Might be an idea to now reboot
