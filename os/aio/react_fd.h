#pragma once

#include <memory>

#include "variable.h"
#include "iom.h"
#include "autofd.h"

class react_fd {
    public:
        react_fd(iomanager &iom, autofd *fd) : iom(iom), fd(new autofd()), canRead(iom), canWrite(iom), error(iom) {}

        variable<bool> canRead, canWrite, error;
    private:
        iomanager &iom;
        std::shared_ptr<autofd> fd;
};
