echo "Some Bash"
cat << EOF > ~/.bashrc
alias ..="cd .."
alias cl='clear'
alias vi='vim'
export PS1="\u@\h \w "
EOF
