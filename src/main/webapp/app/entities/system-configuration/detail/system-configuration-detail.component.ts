import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISystemConfiguration } from '../system-configuration.model';

@Component({
  selector: 'jhi-system-configuration-detail',
  templateUrl: './system-configuration-detail.component.html',
})
export class SystemConfigurationDetailComponent implements OnInit {
  systemConfiguration: ISystemConfiguration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ systemConfiguration }) => {
      this.systemConfiguration = systemConfiguration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
