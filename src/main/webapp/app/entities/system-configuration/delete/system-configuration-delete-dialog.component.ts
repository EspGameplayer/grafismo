import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISystemConfiguration } from '../system-configuration.model';
import { SystemConfigurationService } from '../service/system-configuration.service';

@Component({
  templateUrl: './system-configuration-delete-dialog.component.html',
})
export class SystemConfigurationDeleteDialogComponent {
  systemConfiguration?: ISystemConfiguration;

  constructor(protected systemConfigurationService: SystemConfigurationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.systemConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
